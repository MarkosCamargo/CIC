/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.udesc.ceavi.findnews.leitorrss;

import br.com.udesc.ceavi.findnews.modelo.Noticia;
import com.sun.syndication.feed.synd.SyndEntry;
import com.sun.syndication.feed.synd.SyndFeed;
import com.sun.syndication.io.FeedException;
import com.sun.syndication.io.SyndFeedInput;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import sqlite.Conection;

/**
 *
 * @author marcos
 */
public class IRSSImpl implements IRSS {

    @Override
    public List<Noticia> verificaNoticiasManual() {

        //pegar os sites do banco de dados
        Conection conn = new Conection();
        List<URL> sites = conn.getListaUrl();

        List<String> palavrasChaves = new ArrayList<>();
        palavrasChaves.add("dólar");
        palavrasChaves.add("Dinheiro");
        List<Noticia> noticias = new ArrayList<>();
        for (URL site : sites) {
            Charset inputCharset = Charset.forName("UTF-8");

            HttpURLConnection httpcon = null;
            try {
                httpcon = (HttpURLConnection) site.openConnection();
            } catch (IOException ex) {
                Logger.getLogger(IRSSImpl.class.getName()).log(Level.SEVERE, null, ex);
            }

            // Reading the feed
            SyndFeedInput input = new SyndFeedInput();
            SyndFeed feed = null;
            try {
                feed = input.build(new InputStreamReader(httpcon.getInputStream(), inputCharset));
            } catch (IOException ex) {
                Logger.getLogger(IRSSImpl.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IllegalArgumentException ex) {
                Logger.getLogger(IRSSImpl.class.getName()).log(Level.SEVERE, null, ex);
            } catch (FeedException ex) {
                Logger.getLogger(IRSSImpl.class.getName()).log(Level.SEVERE, null, ex);
            }
            List entries = feed.getEntries();
            Iterator itEntries = entries.iterator();

            while (itEntries.hasNext()) {
                SyndEntry entry = (SyndEntry) itEntries.next();
                for (String chave : palavrasChaves) {
                    if (entry.getTitle().contains(chave) || entry.getDescription().getValue().contains(chave)) {
                        try {
                            noticias.add(new Noticia(entry.getTitle(), entry.getDescription().getValue(), new URL(entry.getLink())));
                        } catch (MalformedURLException ex) {
                            Logger.getLogger(IRSSImpl.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                }
            }
        }
        return noticias;
    }

}
