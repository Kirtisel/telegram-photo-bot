package io.project.Bot.service;

import io.project.Bot.config.BotConfig;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.bots.TelegramWebhookBot;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.GetFile;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.PhotoSize;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class TelegramBot extends TelegramLongPollingBot {

    final BotConfig config;
    private ArrayList<String> captionGirlList = new ArrayList<>();
    private ArrayList<String> captionManList = new ArrayList<>();
    private List<File> girlList = new ArrayList<>();
    private ArrayList<File> manList = new ArrayList<>();
    private ArrayList<File> photoList = new ArrayList<>();
    private HashMap<String, String> captionMap;
    private String serMapName = "./serSrc/captionMap.ser";
    private String photoPath;

    private Pattern pattern = Pattern.compile("/addphoto.*");


    public TelegramBot(BotConfig config) {
        this.config = config;
        this.photoPath = config.getPhotoPath();
        //this.photoPath = new String(config.getPhotoPath().getBytes(StandardCharsets.ISO_8859_1));

        initGirlsList();
        initCaptionGirlList();
        initMansList();
        initCaptionManList();
        initPhotoList();
        initCaptionMap();

        System.out.println("Бот успешно зарегистрирован. Вебхук установлен на: {}" + config.getUrl());

    }

    private void initGirlsList(){
        File file = new File(config.getGirlsPath());
       for (File f : file.listFiles())
           if(f.isFile()) girlList.add(f);
    }

    private void initMansList(){
        File file = new File(config.getManPath());
        for (File f : file.listFiles())
            if(f.isFile()) manList.add(f);
    }

    private void initPhotoList(){
        System.out.println("путь до папки фото " + photoPath);
        System.out.println(Paths.get(photoPath));

        File file = new File(photoPath);
        if(file.listFiles() != null) {
            for (File f : file.listFiles())
                if (f.isFile()) photoList.add(f);
        }
    }

    private void initCaptionGirlList() {
        File file = new File(config.getCaptionGirlPath());
        BufferedReader reader;
        String s="";
        String string="";

        try {
            reader = new BufferedReader(new FileReader(file));
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }


        try {
            string = "";
            do{
                s = reader.readLine();
                if(s != null) string=string.concat(s);
                if(s == null || s.isEmpty()){
                    captionGirlList.add(string);
                    string = "";
                }
            }while (s != null);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        //System.out.println(captionList.get(4));

    }


    private void initCaptionManList() {
        File file = new File(config.getCaptionManPath());
        BufferedReader reader;
        String s="";
        String string="";

        try {
            reader = new BufferedReader(new FileReader(file));
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }


        try {
            string = "";
            do{
                s = reader.readLine();
                if(s != null) string=string.concat(s);
                if(s == null || s.isEmpty()){
                    captionManList.add(string);
                    string = "";
                }
            }while (s != null);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        //System.out.println(captionList.get(4));

    }

    private void initCaptionMap(){
        if (Files.exists(Paths.get(serMapName))){
            //десереализовать мапу
            captionMap = (HashMap<String, String>) deserializeObj(Paths.get(serMapName));
            updateCaptionMap();
            System.out.println("captionMap.size() " + captionMap.size());
        }
        else {
            captionMap = new HashMap<>();
        }
    }

    private void updateCaptionMap(){// создает стринг с именами фоток в папке, потом смотрит есть ли эти названия в ключах
        String names ="";
        for(File f : photoList){ // получаем список имен файлов в папке фото
            names = names.concat(" " + f.getName());
        }
        System.out.println("names" + names);

        Iterator<Map.Entry<String, String>> iterator = captionMap.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<String, String> entry = iterator.next();
            String key = entry.getKey();
            if (!names.contains(key)) {
                // Удаляем элемент, если ключа нет в names
                iterator.remove();
            }
        }
    }





    @Override
    public String getBotUsername() {
        return config.getBotName();
    }

    @Override
    public String getBotToken() {
        return config.getToken();
    }





    private void startCommandRecieved(long chatId, String name){
        String answer = "Привет "+ name + ", мои комманды: " +
                "\n\n /photo получить случайное фото с катки" +
                //"\n\n /woman /man получить случайное фото велосипедиста" +
                "\n\n а если отправишь фотку с подписью /addphoto твое фото добавиться в бот и сможет быть вызвано коммандой /photo";


         sendMessage(chatId, answer);
    }

    private void girlCommandRecieved(long chatId,int messageId){
        sendGirlImage(chatId, messageId);
    }

    private void photoCommandRecieved(long chatId,int messageId){
        sendPhoto(chatId, messageId);
    }

    private void manCommandRecieved(long chatId,int messageId){
        sendManImage(chatId, messageId);
    }

    private void sendMessage(long chatId, String textToSend){
        SendMessage message = new SendMessage();
        message.setChatId(String.valueOf(chatId));
        message.setText(textToSend);
        try{
            execute(message);
        } catch (TelegramApiException e) {

        }
 //       return message;
    }

    private void sendGirlImage(long chatId, int messageId){
        int rnd_girl = random(0, girlList.size()-1);
        int rnd_caption = random(0, captionGirlList.size()-1);
        SendPhoto sendPhoto = new SendPhoto();
        sendPhoto.setChatId(String.valueOf(chatId));
        sendPhoto.setReplyToMessageId(Integer.valueOf(messageId));
        sendPhoto.setCaption(captionGirlList.get(rnd_caption));
        InputFile inputFile = new InputFile();
        inputFile.setMedia(girlList.get(rnd_girl));
        //inputFile.setMedia(new File("//home//user//Изображения//1695982838119955503.jpg"));
        sendPhoto.setPhoto(inputFile);
        try{
            execute(sendPhoto);
        } catch (TelegramApiException e) {

        }
    }

    private void sendPhoto(long chatId, int messageId){
        System.out.println(photoList.size() + "размер листа фото");
        int rnd_photo = random(0, photoList.size()-1);

        int rnd_caption = random(0, captionManList.size()-1);
        SendPhoto sendPhoto = new SendPhoto();
        sendPhoto.setChatId(String.valueOf(chatId));
        sendPhoto.setReplyToMessageId(messageId);

        InputFile inputFile = new InputFile();
        File photo = photoList.get(rnd_photo);
        inputFile.setMedia(photo);

        if (captionMap.get(photo.getName())!= null) // если пользователь добавлял описание оно должно быть в мапе
            sendPhoto.setCaption(captionMap.get(photo.getName()));
        else
            sendPhoto.setCaption(captionManList.get(rnd_caption)); //если нет в мапе берем рандомное описание

        sendPhoto.setPhoto(inputFile);
        try{
            execute(sendPhoto);
        } catch (TelegramApiException e) {

        }
    }

    private void sendManImage(long chatId, int messageId){
        int rnd_man = random(0, manList.size()-1);
        int rnd_caption = random(0, captionManList.size()-1);
        SendPhoto sendPhoto = new SendPhoto();
        sendPhoto.setChatId(String.valueOf(chatId));
        sendPhoto.setReplyToMessageId(messageId);
        sendPhoto.setCaption(captionManList.get(rnd_caption));
        InputFile inputFile = new InputFile();
        inputFile.setMedia(manList.get(rnd_man));
        //inputFile.setMedia(new File("//home//user//Изображения//1695982838119955503.jpg"));
        sendPhoto.setPhoto(inputFile);
        try{
            execute(sendPhoto);
        } catch (TelegramApiException e) {

        }
    }

    private void serializeObj(Object obj, Path p){
        try {
            FileOutputStream fos = new FileOutputStream(p.toString());
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(obj);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private Object deserializeObj (Path p){
        Object obj;
        try {
            FileInputStream fis = new FileInputStream(p.toString());
            ObjectInputStream ois = new ObjectInputStream(fis);
            obj = ois.readObject();
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        return obj;
    }


private int random(int min, int max){
    Random rn = new Random();
    int randomNum = rn.nextInt(max - min + 1) + min;

    return randomNum;
}


    @Override
    public void onUpdateReceived(Update update) {
        if(update.hasMessage() ) {
            if (update.getMessage().hasPhoto()){
                String caption = update.getMessage().getCaption();
                //pattern.matcher(caption);

                if (pattern.matches("/addphoto.*", caption)) {

                    if (photoList.size() >= config.getMaxPhotoVal()) {
                        if (photoList.get(0).delete()) {
                            System.out.println("Файл удален");
                            photoList.remove(0);

                        } else {
                            System.out.println("Файл НЕ удален");
                        }
                        photoList.remove(0);
                    }

                    List<PhotoSize> list = update.getMessage().getPhoto();
                    System.out.println("есть фото");
                    System.out.println("размер = " + list.size());

                    GetFile getFile = new GetFile(list.get(3).getFileId());

                    try {
                        org.telegram.telegrambots.meta.api.objects.File file = execute(getFile);
                        String fileName = update.getMessage().getChat().getUserName() + "\n" + java.time.LocalDate.now()
                                + "\n" + java.time.LocalTime.now();

                        File jFile = new File(photoPath + "/" + fileName);

                        downloadFile(file, jFile);
                        System.out.println("скачал");
                        System.out.println(jFile.getName());

                        if (caption.length() > 10) { // если есть описание добавляем в хэшмэп
                            String userCaption = caption.substring(10);
                            captionMap.put(fileName, userCaption);
                        }
                        System.out.println(captionMap.get(jFile.getName()));

                        serializeObj(captionMap, Paths.get(serMapName));
                        //photoList.add(jFile);


                    } catch (TelegramApiException e) {
                        throw new RuntimeException(e);
                    }
                    initPhotoList();


                }

            } else if (update.getMessage().hasText()) {
                String messageText = update.getMessage().getText();
                long chatId = update.getMessage().getChatId();
                int messageId = update.getMessage().getMessageId();

                switch (messageText){
                    case "/help":
                        startCommandRecieved(chatId, update.getMessage().getChat().getFirstName());
                        break;
                    case "/start":
                        startCommandRecieved(chatId, update.getMessage().getChat().getFirstName());
                        break;
                    case "/":
                        startCommandRecieved(chatId, update.getMessage().getChat().getFirstName());
                        break;
                    case "/woman":
                        girlCommandRecieved(chatId, messageId);
                        break;
                    case "/man":
                        manCommandRecieved(chatId, messageId);
                        break;
                    case "/photo":
                        photoCommandRecieved(chatId,messageId);
                        break;
                    default:
                        //sendMessage(chatId, "Sorry, command was not recognized");
                }
            }
        }
    }
}
