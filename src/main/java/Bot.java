import lombok.SneakyThrows;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.MessageEntity;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

import java.util.*;


public class Bot extends TelegramLongPollingBot {

   public Random rnd = new Random();
   public int a = rnd.nextInt(100);
   String id;

   Set<String> player = new Set<>() {
       @Override
       public int size() {
           return 0;
       }

       @Override
       public boolean isEmpty() {
           return false;
       }

       @Override
       public boolean contains(Object o) {
           return false;
       }

       @Override
       public Iterator<String> iterator() {
           return null;
       }

       @Override
       public Object[] toArray() {
           return new Object[0];
       }

       @Override
       public <T> T[] toArray(T[] a) {
           return null;
       }

       @Override
       public boolean add(String s) {
           return false;
       }

       @Override
       public boolean remove(Object o) {
           return false;
       }

       @Override
       public boolean containsAll(Collection<?> c) {
           return false;
       }

       @Override
       public boolean addAll(Collection<? extends String> c) {
           return false;
       }

       @Override
       public boolean retainAll(Collection<?> c) {
           return false;
       }

       @Override
       public boolean removeAll(Collection<?> c) {
           return false;
       }

       @Override
       public void clear() {

       }
   };
    ArrayList<String> arrayList = new ArrayList<>();
    String[] seasons  = new String[] {"Ты топ", "Чел хорош!", "А ты не плох!", "МЕГАСУПЕРАНСРАЛ"};

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

    private void Goplay(Message message){
try {
    if (message.hasText() && message.hasEntities()){
        Optional<MessageEntity> commandEntity = message.getEntities().stream().filter(e -> "bot_command".equals(e.getType())).findFirst();
        if (commandEntity.isPresent()){
            String command = message.getText().substring(commandEntity.get().getOffset(), commandEntity.get().getLength());
            switch (command){
                case "/play" :
                    id = message.getFrom().getId().toString();
                    player.add(id);
                    execute(SendMessage.builder().chatId(message.getChatId().toString()).text("Привет ты в игре 'Отгадай число от 0 до 100'. Чтобы выйти из игры пропиши /exit. Твой id = " + message.getFrom().getId().toString()).replyToMessageId(message.getMessageId()).build());
                    break;
                case "/exit" :
                    player.remove(id);
                    execute(SendMessage.builder().chatId(message.getChatId().toString()).text("Вы вышли из игры").replyToMessageId(message.getMessageId()).build());
            }
        }
    }

    if (player.contains(id) && message.hasText()) {
       Test(message);
    }


} catch (TelegramApiException e) {
    e.printStackTrace();
}

    }



    private void Test(Message message) {
        try {
            String beta = message.getText();
            int randomTech = rnd.nextInt(3);
            Integer i1 = Integer.valueOf(beta);
            if (a > i1) {
                execute(SendMessage.builder().chatId(message.getChatId().toString()).text("Больше").build());
            } else if (i1 > a) {
                execute(SendMessage.builder().chatId(message.getChatId().toString()).text("Меньше").build());
            } else if (a == i1) {
                String str = Integer.toString(a);
                if (message.hasText()) {
                    if (message.getText().equals(str)) {
                        execute(SendMessage.builder().chatId(message.getChatId().toString()).text(seasons[randomTech]).build());
                        a = rnd.nextInt(100);
                    }
                }
            } else {
                execute(SendMessage.builder().chatId(message.getChatId().toString()).text("Бро зачем?").build());
            }
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
}
