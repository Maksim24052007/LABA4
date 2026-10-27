import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.lang.reflect.Type;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Main {
    public static void main(String[] args) throws IOException {
        Gson gson = new Gson();
        Type listType = new TypeToken<List<Visitor>>() {}.getType();

        List<Visitor> visitors;
        try (Reader reader = new FileReader("book.json")) {
            visitors = gson.fromJson(reader, listType);
        }

        //Задание 1
        System.out.println("--------Задание 1--------");
        visitors.stream()
                .map(v -> v.getName() + " " + v.getSurname())
                .forEach(System.out::println);
        System.out.println("Количество посетителей: " + visitors.stream().count());

        //Задание 2
        System.out.println("\n--------Задание 2--------");
        List<Book> uniqueBooks = visitors.stream()
                .flatMap(v -> v.getFavoriteBooks().stream())
                .distinct()
                .toList();
        uniqueBooks.forEach(b -> System.out.println(b.getName() + " (" + b.getAuthor() + ")"));
        System.out.println("Количество уникальных книг: " + uniqueBooks.size());

        //Задание 3
        System.out.println("\n--------Задание 3--------");
        uniqueBooks.stream()
                .sorted(Comparator.comparingInt(Book::getPublishingYear))
                .forEach(b -> System.out.println(b.getPublishingYear() + " - " + b.getName()));

        //Задание 4
        System.out.println("\n--------Задание 4--------");
        boolean hasAusten = visitors.stream()
                .flatMap(v -> v.getFavoriteBooks().stream())
                .anyMatch(b -> "Jane Austen".equals(b.getAuthor()));
        System.out.println("Книга Jane Austen в избранном есть: " + hasAusten);

        //Задание 5
        System.out.println("\n--------Задание 5--------");
        int maxBooks = visitors.stream()
                .mapToInt(v -> v.getFavoriteBooks().size())
                .max()
                .orElse(0);
        System.out.println("Максимальное число избранных книг: " + maxBooks);

        //Задание 6
        System.out.println("\n--------Задание 6--------");
        double avg = visitors.stream()
                .mapToInt(v -> v.getFavoriteBooks().size())
                .average()
                .orElse(0);
        System.out.println("Среднее число избранных книг: " + avg);

        Map<String, List<Sms>> grouped = visitors.stream()
                .filter(Visitor::isSubscribed)
                .map(v -> {
                    int n = v.getFavoriteBooks().size();
                    String msg = n > avg ? "you are a bookworm"
                            : n < avg ? "read more"
                            : "fine";
                    return new Sms(v.getPhone(), msg);
                })
                .collect(Collectors.groupingBy(Sms::getMessage));

        grouped.forEach((message, list) -> {
            System.out.println("Категория \"" + message + "\":");
            list.forEach(System.out::println);
        });
    }
}