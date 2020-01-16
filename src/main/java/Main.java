import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class Main {
    public static void main(String[] args) {
        String link = "https://lenta.ru/";
        ArrayList<URL> imageUrlLinks = parseImageFromHtml(link);
        try {
            for (URL imageLink : imageUrlLinks) {
                String formatName = imageLink.toString(); //Будем хранить расширение файла
                formatName = formatName.substring(formatName.length() - 3);//Получаем расширение (последние 4 символа)
                String fileName = imageLink.toString(); //Имя файла, содержится после нижнего подчёркивания
                fileName = fileName.substring(fileName.indexOf("_") + 1);
                BufferedImage image = ImageIO.read(imageLink);
                File directory = new File("src/main/data");
                if (!directory.exists()) { //Если это папки не существует, надо её создать
                    directory.mkdir();
                    System.out.println("Folder " + directory + " was created");
                }
                File file = new File("src/main/data/" + fileName);
                ImageIO.write(image, formatName, file); //Пишем изобращение в файл
                System.out.println("File " + file + " created successful");
            }
            System.out.println("All images downloaded!");
        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

    private static ArrayList<URL> parseImageFromHtml(String link) { //Метод получения листа URL-ов картинок
        ArrayList<URL> imageList = new ArrayList<>();
        try {
            Document document = Jsoup.connect(link).get();
            Elements elements = document.select("img.g-picture");
            elements.forEach(element -> {
                try {
                    URL url = new URL(element.absUrl("src"));
                    imageList.add(url);
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }
            });
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return imageList;
    }
}
