package com.dantonov.musicstore.controller;

import com.dantonov.musicstore.dto.AlbumDto;
import com.dantonov.musicstore.dto.ResponseMessageDto;
import com.dantonov.musicstore.entity.Album;
import com.dantonov.musicstore.entity.Author;
import com.dantonov.musicstore.entity.Genre;
import com.dantonov.musicstore.entity.Track;
import com.dantonov.musicstore.entity.User;
import com.dantonov.musicstore.exception.AlreadyBoughtException;
import com.dantonov.musicstore.exception.NotEnoughMoneyException;
import com.dantonov.musicstore.exception.PageNotFoundException;
import com.dantonov.musicstore.exception.RequestDataException;
import com.dantonov.musicstore.exception.ResourceNotFoundException;
import com.dantonov.musicstore.exception.UnauthorizedResourceException;
import com.dantonov.musicstore.service.AlbumService;
import com.dantonov.musicstore.service.DataManagementService;
import com.dantonov.musicstore.service.GenreService;
import com.dantonov.musicstore.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.jaudiotagger.audio.mp3.ByteArrayMP3AudioHeader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Antonov Denis (den007230@gmail.com)
 */
@Controller("AlbumController")
@RequestMapping(value = "/author/{authorName}/album")
public class AlbumController {
    
    private static final Logger log = LoggerFactory.getLogger(AlbumController.class);
    
    
    @Autowired
    private GenreService genreService;
    
    @Autowired
    private DataManagementService dataService;
    
    @Autowired
    private AlbumService albumService;

    @Autowired
    private UserService userService;

    @Autowired
    private SimpleDateFormat dateFormat;

    @Autowired
    private DecimalFormat decimalFormat;
    
    
    
    @RequestMapping(value = "/{albumName}", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public ModelAndView albumPage(@PathVariable("albumName") final String albumName,
                                  @PathVariable("authorName") final String authotName,
                                  final ModelAndView modelAndView,
                                  final Authentication authentication) {
        
        final Album album = albumService.findByTitleAndAuthor(albumName, authotName);
        if (album == null) {
            throw new PageNotFoundException();
        }

        final User user = (authentication == null)
                ? null
                : userService.findByLogin(authentication.getName());
        
        if (user != null) {
            modelAndView.addObject("isBought", user.hasAlbum(album));
        } else {
            modelAndView.addObject("isBought", false);
        }
        
        modelAndView.addObject("album", album);
        modelAndView.addObject("dateFormat", dateFormat);
        modelAndView.addObject("format", decimalFormat);
        modelAndView.addObject("genres", genreService.findAll(true));
        modelAndView.addObject("user", user);
        
        modelAndView.setViewName("album");
        return modelAndView;
    }

    @RequestMapping(value = "/{albumName}/buy", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE) 
    @ResponseStatus(HttpStatus.OK)
    public ResponseMessageDto buy(@PathVariable("authorName") final String authorName,
                                  @PathVariable("albumName") final String albumName,
                                  final Authentication authentication) {

        final User user = userService.findByLogin(authentication.getName());
        
        final Album album = albumService.findByTitleAndAuthor(albumName, authorName);
        if (user.hasAlbum(album)) {
            log.warn("У пользователя {} уже куплен альбом {}", user.getLogin(), album.getTitle());
            throw new AlreadyBoughtException("Альбом " + album.getTitle() + "уже куплен.");
        }
        
        try {
            albumService.buy(album, user, album.getPrice());
        } catch (NotEnoughMoneyException ex) {
            log.warn("У пользователя {} недостаточно денег({}р) для покупки альбома({}p)", 
                       user.getLogin(), user.getWallet(), album.getPrice());
            throw ex;
        }
            
        return new ResponseMessageDto(HttpStatus.OK.value(), "Альбом " + album.getTitle() + " успешно куплен.");
    }

    @RequestMapping(method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public ResponseMessageDto createAlbum(@RequestParam("album") final String albumDtoStr,
                                          @RequestParam("cover") final MultipartFile cover,
                                          @RequestParam("tracks[]") final MultipartFile[] tracks,
                                          @PathVariable("authorName") final String authorName,
                                          final Authentication authentication) {

        final User user = userService.findByLogin(authentication.getName());


        final Author author = user.getAuthor();
        if (author == null || !author.getName().equals(authorName)) {
            log.warn("Ошибка при добавлени альбома. Аккаунт с логином {} не является вадельцем группы.", user.getLogin());
            throw new UnauthorizedResourceException("Ошибка при добавлени альбома. Аккаунт с логином " + user.getLogin() + " не является вадельцем группы.");
        }

        final ObjectMapper mapper = new ObjectMapper();
        AlbumDto albumDto = null;
        try {
            albumDto = mapper.readValue(albumDtoStr, AlbumDto.class);
        } catch (IOException ex) {
            log.warn("Ошибка при добавлени альбома. Неверные данные.", ex);
            throw new RequestDataException("Неверно введены данные.");
        }
        
        final Album album = createAlbum(albumDto);
        album.setAuthor(author);
        try {
            album.setTracks(createTrackList(albumDto.getSongsTitles(), tracks, album));
        } catch (IOException ex) {
             log.warn("Ошибка при добавлени альбома. Ошибка при обработке файлов.", ex);
             throw new RequestDataException("Ошибка при обработке файлов.");
        }

        final List<Genre> genres = getGenres(albumDto.getGenresIds());
        
        albumService.create(album, user, cover, tracks, genres);
        
        return new ResponseMessageDto(HttpStatus.OK.value(), "Альбом " + album.getTitle() + " успешно добавлен.");
    }

    @RequestMapping(value = "/{albumName}/track/{trackName}", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public FileSystemResource getTrack(@PathVariable("authorName") final String author,
                                       @PathVariable("albumName") final String albumTitle,
                                       @PathVariable("trackName") final String track,
                                       final Authentication authentication) {
        final User user = userService.findByLogin(authentication.getName());
        
        final Album album = albumService.findByTitleAndAuthor(albumTitle, author);
        if (album == null) {
            throw new ResourceNotFoundException("Альбом '" + albumTitle + "' группы '" + author + "' не найден.");
        }
        
        if (!user.hasAlbum(album)) {
            throw new UnauthorizedResourceException("Альбом '" + albumTitle + "' не кулен");
        }
        
        final FileSystemResource trackResource = new FileSystemResource(dataService.getTrack(author, albumTitle, track));
        if (!trackResource.exists()) {
            throw new ResourceNotFoundException("Трек '" + track + "' Альбома '" + albumTitle + "' группы '" + author + "' не найден.");
        }
        return trackResource;
    }
    
    
    private Album createAlbum(final AlbumDto albumDto) {
        final Album result = new Album();
        
        BigDecimal bdValue = null;
        try {
            bdValue = new BigDecimal(albumDto.getPrice());
            if (bdValue.compareTo(BigDecimal.ZERO) <= 0) {
                throw new RuntimeException();
            }
        } catch (Exception ex) {
            log.warn("Не удалось сохранить альбом. Пользователь неверно ввел сумму ({}).", albumDto.getPrice(), ex);
            throw new RequestDataException("Некорректно задана сумма.");
        }
        
        result.setDesc(albumDto.getDesc());
        result.setPrice(bdValue);
        result.setTitle(albumDto.getTitle());
        result.setqSold(0L);
        
        try {
            result.setReleaseDate(dateFormat.parse(albumDto.getReleaseDate()));
        } catch (Exception ex) {
            log.warn("Не удалось сохранить альбом. Пользователь неверно задал дату ({}).", albumDto.getReleaseDate());
            throw new RequestDataException("Некорректно задана сумма.");
        }
        
        return result;
    }
    
    private List<Track> createTrackList(final List<String> titles, final MultipartFile[] tracks, final Album album)
                                        throws IOException {
        
        final List<Track> result = new ArrayList<>();
        try {
            for (byte i = 0; i < tracks.length; i++) {
                final Track track = new Track();
                track.setName(titles.get(i));
                track.setPosition((byte)(i + 1));
                track.setSize(tracks[i].getSize() >> 10);

                final ByteArrayMP3AudioHeader header = new ByteArrayMP3AudioHeader(tracks[i].getBytes());
                final int bitrate = (int)header.getBitRateAsNumber();
                track.setBitrate(bitrate);
                track.setDuration(header.getTrackLength());
                track.setAlbum(album);

                result.add(track);
            }
        } catch (RuntimeException ex) {
            log.warn("Ошибка при добавлени альбома.", ex);
            throw new RequestDataException("Ошибка при добавлени альбома.");
        }
        return result;
    }
    
    private List<Genre> getGenres(final String genres) {
        final String[] genreIds =  genres.split(",");
        final List<Genre> genreList = new ArrayList<>();
        for(final String genreId : genreIds) {
            final Genre genre = genreService.findById(Integer.parseInt(genreId));
            if (genre == null) {
                log.warn("Не удалось сохранить альбом. Жанр с id = {} не найден.", Integer.parseInt(genreId));
                throw new RequestDataException("Жанр не найден.");
            }
            genreList.add(genre);
        }
        return genreList;
    }
    
    
}
