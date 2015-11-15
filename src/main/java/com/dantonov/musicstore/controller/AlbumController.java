package com.dantonov.musicstore.controller;

import com.dantonov.musicstore.dto.AlbumDto;
import com.dantonov.musicstore.entity.Album;
import com.dantonov.musicstore.entity.Author;
import com.dantonov.musicstore.entity.Genre;
import com.dantonov.musicstore.entity.Track;
import com.dantonov.musicstore.entity.User;
import com.dantonov.musicstore.exception.NotEnoughMoneyException;
import com.dantonov.musicstore.service.AlbumService;
import com.dantonov.musicstore.service.AuthService;
import com.dantonov.musicstore.service.DataManagementService;
import com.dantonov.musicstore.service.GenreService;
import com.dantonov.musicstore.service.UserService;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.HttpServletRequest;


import javax.sound.sampled.UnsupportedAudioFileException;
import org.jaudiotagger.audio.mp3.ByteArrayMP3AudioHeader;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

/**
 *
 * @author Antonov Denis (den007230@gmail.com)
 */
@Controller("AlbumController")
@RequestMapping(value = "/author/{authorName}/album")
public class AlbumController {
    
    private static final Logger log = LoggerFactory.getLogger(AlbumController.class);
    private static final String AUTHOR_ROLE = "AUTHOR";
    private static final SimpleDateFormat REQUEST_DATE_FORMAT = new SimpleDateFormat("dd.MM.yyyy");
    private static final DecimalFormat DEC_FORMAT = new DecimalFormat();
    static {
        DEC_FORMAT.setMaximumFractionDigits(2);
        DEC_FORMAT.setMinimumFractionDigits(2);
        DEC_FORMAT.setGroupingUsed(false);
    }
    
    @Autowired
    private GenreService genreService;
    
    @Autowired
    private UserService userService;
    
    @Autowired
    private DataManagementService dataService;
    
    @Autowired
    private AlbumService albumService;
    
    @Autowired
    private AuthService authService;
    
    
    @RequestMapping(value = "/{albumName}", method = RequestMethod.GET)
    public ModelAndView albumPage(@PathVariable("albumName") String albumName,
                                  @PathVariable("authorName") String authotName,
                                   ModelAndView modelAndView,
                                   HttpServletRequest request) {
        
        Album album = albumService.findByTitleAndAuthor(albumName, authotName);
        if (album == null) {
            modelAndView.setViewName("redirect:/");
            return modelAndView;
        }
        
        User user = authService.getUser(request);
        if (user != null) {
            modelAndView.addObject("user", user);
            modelAndView.addObject("isBought", user.hasAlbum(album));
        } else {
            modelAndView.addObject("isBought", false);
        }
        
        List<Genre> genres = genreService.findAll();
        modelAndView.addObject("genres", genres);
        
        modelAndView.addObject("album", album);
        modelAndView.addObject("dateFormat", REQUEST_DATE_FORMAT);
        modelAndView.addObject("format", DEC_FORMAT);
        
        modelAndView.setViewName("album");
        return modelAndView;
    }
    
    @RequestMapping(value = "/{albumName}/buy", method = RequestMethod.PUT) 
    public void buy(@PathVariable("authorName") String authorName,
                    @PathVariable("albumName") String albumName,
                    HttpServletRequest request) {
        
        User user = authService.getUser(request);
        if (user == null) {
            log.warn("Ошибка при покупке. Пользователь с логином не найден.");
            return;
        }
        //проверка на то, что альбом уже куплен
        Album album = albumService.findByTitleAndAuthor(albumName, authorName);
        
        try {
            albumService.buy(album, user, album.getPrice());
        } catch (NotEnoughMoneyException ex) {
            log.warn("У пользователя {} недостаточно денег({}р) для покупки альбома({}p)", 
                       user.getLogin(), user.getWallet(), album.getPrice());
        }
            
            
        
    }
    
    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public void createAlbum(@RequestParam("album") String albumDtoStr,
                            @RequestParam("cover") MultipartFile cover,
                            @RequestParam("tracks[]") MultipartFile[] tracks,
                            HttpServletRequest request) {
        
        try {
            User user = authService.getUser(request);
            if (user == null) {
                log.warn("Ошибка при добавлени альбома. Пользователь с логином не найден.");
                return;
            }
            
            Author author = user.getAuthor();
            if (author == null) {
                log.warn("Ошибка при добавлени альбома. Аккаунт с логином {} не принадлежит группе.", user.getLogin());
                return;
            }
            
            if (!user.hasRole(AUTHOR_ROLE)) {
                log.warn("Ошибка при добавлени альбома. Аккаунт с логином {} не содержит роль AUTHOR.", user.getLogin());
            }
            
            ObjectMapper mapper = new ObjectMapper();
            AlbumDto albumDto = mapper.readValue(albumDtoStr, AlbumDto.class);
            Album album = createAlbum(albumDto, author);
            album.setAuthor(author);
            album.setTracks(createTrackList(albumDto.getSongsTitles(), tracks, album));
            
            albumService.save(album);
            dataService.saveAlbumData(author.getName(), album.getTitle(), cover, tracks);
            albumService.buy(album, user, BigDecimal.ZERO);
            
        } catch (IOException ex) {
            log.warn("Ошибка при добавлени альбома. Ошибка при парсинге json строки.", ex);
        } catch (ParseException ex) {
            log.warn("Ошибка при добавлени альбома. Ошибка при парсинге.", ex);
        } catch (NullPointerException ex) {
            log.warn("Ошибка при добавлени альбома.", ex);
        } catch (UnsupportedAudioFileException ex) {
            log.warn("Ошибка при добавлени альбома. Неверный формат аудио файла.", ex);
        } catch (NotEnoughMoneyException ex) {
            log.warn("Ошибка при добавлени альбома. Xoxoxo.");
        } catch (Exception ex) {
            log.warn("Ошибка при добавлени альбома. Xoxoxo.", ex);
        }
    }
    
    @RequestMapping(value = "/{albumName}/track/{trackName}", 
                    method = RequestMethod.GET)
    @ResponseBody
    public FileSystemResource getTrack(@PathVariable("authorName") String author,
                                       @PathVariable("albumName") String albumTitle,
                                       @PathVariable("trackName") String track,
                                       HttpServletRequest request) {
        User user = authService.getUser(request);
        if (user == null) {
            return null;
        }
        
        Album album = albumService.findByTitleAndAuthor(author, albumTitle);
        if (album == null) {
            return null;
        }
        
        if (!user.hasAlbum(album)) {
            return null;
        }
        
        FileSystemResource trackResource = new FileSystemResource(dataService.getTrack(author, albumTitle, track));
        if (!trackResource.exists()) {
            return null;
        }
        return trackResource;
    }
    
    
    private Album createAlbum(AlbumDto albumDto, Author author) throws ParseException {
        Album result = new Album();
        
        result.setDesc(albumDto.getDesc());
        result.setPrice(new BigDecimal(albumDto.getPrice()));
        result.setTitle(albumDto.getTitle());
        result.setqSold(0L);
        result.setReleaseDate(REQUEST_DATE_FORMAT.parse(albumDto.getReleaseDate()));
        
        String[] genreIds =  albumDto.getGenresIds().split(",");
        List<Genre> genres = new ArrayList<>();
        for(String genreId : genreIds) {
            Genre genre = genreService.findById(Integer.parseInt(genreId));
            if (genre == null) {
                throw new NullPointerException("Жанр с id = " + Integer.parseInt(genreId) + " не найден.");
            }
            genre.getAlbums().add(result);
            genres.add(genre);
        }
        
        result.setGenres(genres);
        
        return result;
    }
    
    private List<Track> createTrackList(List<String> titles, MultipartFile[] tracks, Album album)
                                        throws IOException, UnsupportedAudioFileException {
        
        List<Track> result = new ArrayList<>();
        for (byte i = 0; i < tracks.length; i++) {
            Track track = new Track();
            track.setName(titles.get(i));
            track.setPosition((byte)(i + 1));
            track.setSize(tracks[i].getSize());
            
            ByteArrayMP3AudioHeader header = new ByteArrayMP3AudioHeader(tracks[0].getBytes());
            
            int bitrate = (int)header.getBitRateAsNumber();
            track.setBitrate(bitrate);
            track.setDuration((int)(tracks[i].getSize() >> 10) / bitrate);
            track.setAlbum(album);
            
            result.add(track);
        }
        return result;
    }
}
