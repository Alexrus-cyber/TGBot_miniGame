import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.MessageEntity;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

import javax.xml.stream.XMLStreamReader;
import java.util.*;


public class Bot extends TelegramLongPollingBot {

   public Random rnd = new Random();
   public int a = rnd.nextInt(100);
    Set<String> players = new HashSet();
    String[] seasons  = new String[] {"Ты топ", "Чел хорош!", "А ты не плох!", "МЕГАСУПЕРАНСРАЛ"};
    String id;


    public static void main(String[] args) {
        try {
            Bot bot = new Bot();
            TelegramBotsApi telegramBotsApi = new TelegramBotsApi(DefaultBotSession.class);
            telegramBotsApi.registerBot(bot);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }

    }

    @Override
    public String getBotUsername() {
        return System.getenv("BOT_NAME");
    }

    @Override
    public String getBotToken() {
        return System.getenv("BOT_TOKEN");
    }


    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage())
            goPlay(update.getMessage());
     }

    private void goPlay(Message message){
        try {
            if (message.hasEntities()){
            Optional<MessageEntity> commandEntity = message.getEntities().stream().filter(e -> "bot_command".equals(e.getType())).findFirst();
                if (commandEntity.isPresent()){
                    String command = message.getText().substring(commandEntity.get().getOffset(), commandEntity.get().getLength());
                switch (command){
                    case "/play" :
                        id = message.getFrom().getId().toString();
                        players.add(id);
                        execute(SendMessage.builder().chatId(message.getChatId().toString()).text("Привет ты в игре 'Отгадай число от 0 до 100'. Чтобы выйти из игры пропиши /exit. Твой id = " + id).replyToMessageId(message.getMessageId()).build());
                        break;
                    case "/exit" :
                    players.remove(id);
                    execute(SendMessage.builder().chatId(message.getChatId().toString()).text("Вы вышли из игры").replyToMessageId(message.getMessageId()).build());
                }
                //Выход после обработки команды тк дальше идет проверка числа
                return;
            }
        }
            if (players.contains(id)) {
                checkAnswer(message);
            }
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    private void checkAnswer(Message message) {
        try {
            String beta = message.getText();
            Integer i1 = Integer.valueOf(beta);
            if (a > i1) {
                execute(SendMessage.builder().chatId(message.getChatId().toString()).text("Больше").build());
            } else if (i1 > a) {
                execute(SendMessage.builder().chatId(message.getChatId().toString()).text("Меньше").build());
            }
            else{
            int randomTech = rnd.nextInt(3);
            execute(SendMessage.builder().chatId(message.getChatId().toString()).text(seasons[randomTech]).build());
            a = rnd.nextInt(100);

        }
        }catch (Exception e) {
            e.printStackTrace();
        }
    }
}
