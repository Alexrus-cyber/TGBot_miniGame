import javassist.compiler.ast.Member;
import lombok.SneakyThrows;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.MessageEntity;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

import java.util.ArrayList;
import java.util.Optional;
import java.util.Random;


public class Bot extends TelegramLongPollingBot {

   public Random rnd = new Random();
   public int a = rnd.nextInt(100);
    ArrayList<String> arrayList = new ArrayList<>();

    byte check = 0;

    @SneakyThrows
    public static void main(String[] args) {
        Bot bot = new Bot();
        TelegramBotsApi telegramBotsApi = new TelegramBotsApi(DefaultBotSession.class);
        telegramBotsApi.registerBot(bot);
    }

    @Override
    public String getBotUsername() {
        return System.getenv("BOT_NAME");
    }

    @Override
    public String getBotToken() {
        return System.getenv("BOT_TOKEN");
    }

    @SneakyThrows
    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage()){
                Goplay(update.getMessage());
        }
    }
    @SneakyThrows
    private void Goplay(Message message){
        if (message.hasText() && message.hasEntities()){
            Optional<MessageEntity> commandEntity = message.getEntities().stream().filter(e -> "bot_command".equals(e.getType())).findFirst();
            if (commandEntity.isPresent()){
               String command = message.getText().substring(commandEntity.get().getOffset(), commandEntity.get().getLength());
               switch (command){
                   case "/play" :
                     arrayList.add(message.getChatId().toString());
                     check = (byte)(check + 1);
                     execute(SendMessage.builder().chatId(message.getChatId().toString()).text("Привет ты в игре 'Отгадай число от 0 до 100'. Чтобы выйти из игры пропиши /exit. Твой id = " + message.getChatId().toString()).replyToMessageId(message.getMessageId()).build());
                     break;
                     case "/exit" :
                       arrayList.remove(message.getChatId().toString());
                       check = (byte)(check - 1);;
                       execute(SendMessage.builder().chatId(message.getChatId().toString()).text("Вы вышли из игры").replyToMessageId(message.getMessageId()).build());
               }
            }
        }
        for (int i = 0; i < 1; i++) {
            if (arrayList.get(i).equals(message.getChatId().toString()) && message.hasText() && check == 1) {
                Test(message);
            }
        }

        if (check != 1) {
            for (int i = 0; i < 1; i++) {
                if (arrayList.get(0).equals(message.getChatId().toString()) && message.hasText()) {
                    Test(message);
                }
            }
        }
    }
    @SneakyThrows
    private void Test(Message message){
                String beta = message.getText();
                Integer i1 = Integer.valueOf(beta);
                if (a > i1) {
                    execute(SendMessage.builder().chatId(message.getChatId().toString()).text("Больше").build());
                } else if (i1 > a) {
                    execute(SendMessage.builder().chatId(message.getChatId().toString()).text("Меньше").build());
                } else if (a == i1) {
                    String str = Integer.toString(a);
                    if (message.hasText()) {
                        if (message.getText().equals(str)) {
                            execute(SendMessage.builder().chatId(message.getChatId().toString()).text("Ты топ").build());
                            a = rnd.nextInt(100);
                        }
                    }
                }else{ execute(SendMessage.builder().chatId(message.getChatId().toString()).text("Бро зачем?").build());}
            }
}
