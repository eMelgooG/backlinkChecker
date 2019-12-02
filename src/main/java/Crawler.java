import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map.Entry;

import org.apache.commons.collections4.MultiValuedMap;
import org.apache.commons.collections4.multimap.HashSetValuedHashMap;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class Crawler {

    private static final String DOMAIN = "google.com";
    private static final boolean SAME_DOMAIN = false;
    private static final int MAX_DEPTH = 10;
    private MultiValuedMap < String, Out > inOut;

    public Crawler() {
        inOut = new HashSetValuedHashMap < > ();
    }

    public static String getDomainName(String url) {
        String domain = null;

        try {

            URI uri = new URI(url);
            domain = uri.getHost();
            domain = domain.startsWith("www.") ? domain.substring(4) : domain;

            if (domain.startsWith("mailto:")) {
                domain = null;
            }

        } catch (URISyntaxException e) {

        }

        return domain;
    }

    public void crawl(String URL, int depth) {
        System.out.println("Crawling Depth =  " + depth + " | URL = " + URL);

        try {
            String domain = getDomainName(URL);

            if (domain != null && !inOut.containsKey(URL)
                    && (!SAME_DOMAIN || domain.equals(DOMAIN)) &&
                    (depth < MAX_DEPTH)) {
                Document document = Jsoup.connect(URL).get();
                Elements ahrefs = document.select("a[href]");

                depth++;

                for (Element ahref: ahrefs) {
                    String dest = ahref.attr("abs:href");
                    String anchor = ahref.text();
                    inOut.put(URL, new Out(dest, anchor));

                    crawl(dest, depth);
                }
            }

        } catch (Exception e) {
            // ... Error for URL ...
        }
    }

    public void print() {
        for (Entry < String, Collection < Out >> links: inOut.asMap().entrySet()) {
            System.out.println("\n" + links.getKey() + "\n");

            for (Out link: links.getValue()) {
                System.out.println("\t" + link.href + " | Anchor = " + link.anchor);
            }

        }
    }

    public void printBacklinks(String url) {
        List < Tuple > list = new ArrayList < > ();

        for (Entry < String, Collection < Out >> links: inOut.asMap().entrySet()) {
            String in = links.getKey();

            for (Out out: links.getValue()) {
                if (out.href.equals(url)) {
                    Tuple t = Tuple.GENERIC;
                    t.href = in ;

                    if (list.contains(t)) {
                        int index = list.indexOf(t);
                        t = list.get(index);
                    } else {
                        t = new Tuple( in );
                    }

                    t.addValue(out.anchor);

                    if (!list.contains(t)) {
                        list.add(t);
                    }
                }
            }
        }

        System.out.println("\nURL = " + url);
        System.out.println("Nb backlinks = " + list.size());

        for (Tuple link: list) {
            System.out.print("\t" + link.href + " | Keywords = ");

            for (String w: link.anchors) {
                System.out.print(w + ", ");
            }

            System.out.println();
        }

    }


    public static void main(String[] args) {
        Crawler crawler = new Crawler();
        crawler.crawl("https://google.com", 0);
        //crawler.print();
      //  crawler.printBacklinks("https://google.com/");
    }
}



