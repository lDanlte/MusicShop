package com.dantonov.musicstore.controller;

import com.dantonov.musicstore.dto.AlbumDto;
import com.dantonov.musicstore.entity.Album;
import com.dantonov.musicstore.entity.Author;
import com.dantonov.musicstore.entity.Genre;
import com.dantonov.musicstore.entity.Track;
import com.dantonov.musicstore.entity.User;
import com.dantonov.musicstore.exception.NotEnoughMoneyException;
import com.dantonov.musicstore.service.AlbumService;
import com.dantonov.musicstore.service.DataManagementService;
import com.dantonov.musicstore.service.GenreService;
import com.dantonov.musicstore.service.UserService;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;


import javax.sound.sampled.UnsupportedAudioFileException;
import org.jaudiotagger.audio.mp3.ByteArrayMP3AudioHeader;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
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
    private static final SimpleDateFormat REQUEST_DATE_FORMAT = new SimpleDateFormat("dd.MM.yyyy");
    
    
    @Autowired
    private GenreService genreService;
    
    @Autowired
    private UserService userService;
    
    @Autowired
    private DataManagementService dataService;
    
    @Autowired
    private AlbumService albumService;
    
    
    @RequestMapping(value = "/{albumName}", method = RequestMethod.GET)
    public ModelAndView albumPage(@PathVariable("albumName") String albumName, ModelAndView modelAndView) {
        modelAndView.addObject("isBought", false);
        
        List<Genre> genres = genreService.findAll();
        modelAndView.addObject("genres", genres);
        
        modelAndView.setViewName("album");
        
        return modelAndView;
    }
    
    @RequestMapping(value = "/{albumName}/buy", method = RequestMethod.PUT) 
    public void buy(@PathVariable("authorName") String authorName,
                    @PathVariable("albumName") String albumName,
                    @RequestParam("login") String login) { ///***TEMP VARIABLE. DELETE AFTER ADDING SECURITY***///
        
        User user = userService.findByLogin(login);
        if (user == null) {
            log.warn("Ошибка при покупке. Пользователь с логином {} не найден.", login);
            return;
        }
        
        Album album = albumService.findByTitleAndAuthor(albumName, authorName);
        
        try {
            albumService.buy(album, user, album.getPrice());
        } catch (NotEnoughMoneyException ex) {
            log.warn("У пользователя {} недостаточно денег({}р) для покупки альбома({}p)", 
                       login, user.getWallet(), album.getPrice());
        }
            
            
        
    }
    
    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public void createAlbum(@RequestParam("login") String login, ///***TEMP VARIABLE. DELETE AFTER ADDING SECURITY***///
                            @RequestParam("album") String albumDtoStr,
                            @RequestParam("cover") MultipartFile cover,
                            @RequestParam("tracks[]") MultipartFile[] tracks) {
        
        try {
            User user = userService.findByLogin(login);
            if (user == null) {
                log.warn("Ошибка при добавлени альбома. Пользователь с логином {} не найден.", login);
                return;
            }
            
            Author author = user.getAuthor();
            if (author == null) {
                log.warn("Ошибка при добавлени альбома. Аккаунт с логином {} не принадлежит группе.", login);
                return;
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
            track.setBitrate((int)header.getBitRateAsNumber());
            track.setDuration(header.getTrackLength());
           /* try(BufferedInputStream inputStream = new BufferedInputStream(tracks[i].getInputStream())) {
                AudioInputStream audioStream = AudioSystem.getAudioInputStream(inputStream);
                AudioFormat audioFormat = audioStream.getFormat();
                double durationInSeconds = audioStream.getFrameLength() / audioFormat.getFrameRate();
                int bitrate = (int) ((long) (tracks[i].getSize() / durationInSeconds) >> 10);
                track.setBitrate(bitrate);
                track.setDuration((int)(durationInSeconds));
                track.setAlbum(album);
                audioStream.close();
            }*/
            track.setAlbum(album);
            result.add(track);
        }
        return result;
    }
}
