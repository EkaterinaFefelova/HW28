import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

public class Main {
    public static final long KB_SIZE = 1024;
    public static final long MB_SIZE = KB_SIZE*1024;
    public static final long GB_SIZE = MB_SIZE*1024;
    public static final long TB_SIZE = GB_SIZE*1024;

    public static void main(String[] args) {
        System.out.println("============= ДЗ 2 =============");
        System.out.println("Введите путь к файлу или папке: ");
        String path = new Scanner(System.in).nextLine();
        printSize(path);

        System.out.println("============= ДЗ 3 =============");
        setSale();
    }

    private static void printSize(String path) {
        long size = 0;
        File file = new File(path);
        try {
            if (file.isFile())
                size = file.length();
            else {
                size = Files.walk(Path.of(path))
                        .map(Path::toFile)
                        .filter(File::isFile)
                        .mapToLong(File::length).sum();
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        sizeConverter(size);
    }

    private static void sizeConverter(double size) {
        System.out.println("Размер файла/папки составляет " +
                (size < KB_SIZE ? size + " байт" :
                        size < MB_SIZE ? String.format("%.2f", size / KB_SIZE) + " Кб" :
                                size < GB_SIZE ? String.format("%.2f", size / MB_SIZE) + " Мб" :
                                        size < TB_SIZE ? String.format("%.2f", size / GB_SIZE) + " Гб" :
                                                size < (TB_SIZE * 1024) ? String.format("%.2f", size / TB_SIZE) + " Тб" :
                                                        "Невероятно огромное число более 1024 Тб!"));
    }

    //Решение без создания класса Car
    /*private static void setSale() {
        try {
            List <String[]> cars = Files.readAllLines(Path.of("HW28/data/car_price.txt")).stream()
                    .map(s->s.split(" "))
                    .collect(Collectors.toList());

           cars.stream()
                    .filter(s -> Integer.parseInt(s[1] ) < LocalDate.now().getYear()-5)
                    .forEach(s -> s[2] = String.valueOf((int)(Integer.parseInt(s[2]) * 0.95)));

           cars.stream()
                   .filter(s -> Integer.parseInt(s[1] ) >= LocalDate.now().getYear()-5)
                   .forEach(s -> s[2] = String.valueOf((int)(Integer.parseInt(s[2]) * 0.98)));

           List<String> newCars = cars.stream().map(s->s[0] + " " + s[1] + " " + s[2])).collect(Collectors.toList());

           Files.write(Path.of("HW28/data/car_price2.txt"),newCars);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }*/


    //Решение c созданием объекта Car
    private static void setSale() {
    try {
            List <Car> cars = Files.readAllLines(Path.of("HW28/data/car_price.txt")).stream()
                    .map(l -> l.split(" "))
                    .map( s -> new Car(s[0], Integer.parseInt(s[1]), Double.parseDouble(s[2])))
                    .collect(Collectors.toList());

            /*альтернативная запись
            List <Car> cars = Files.readAllLines(Path.of("HW28/data/car_price.txt")).stream()
                    .map(s-> {
                    String[] items = s.split("\\s");
                    if (items.length != 3) {
                        throw new RuntimeException ("Wrong line!);
                    }
                    return new Car(items[0],
                        Integer.parseInt(items[1]),
                        Double.parseDouble(items[2])
                    );
              }).collect(Collectors.toList());
             */

            cars.stream()
                    .filter(c -> c.getYear() < LocalDate.now().getYear()-5)
                    .forEach(c-> c.setPrice(c.getPrice()*0.95));
            cars.stream()
                    .filter(c -> c.getYear() >= LocalDate.now().getYear()-5)
                    .forEach(c-> c.setPrice(c.getPrice()*0.98));

            Files.write(Path.of("HW28/data/car_price2.txt"), cars.stream().
                    map(c-> c.getName() + " " + c.getYear() + " " + String.format("%.0f", c.getPrice())).
                    collect(Collectors.toList()));

            /* альтернативный вариант
            int currentYear = LocalDate.now().getYear();
            List <String> carsNewPrice = cars.stream()
                    .map(car -> {
                    double coefficient = car.getYear() < currentYear-5 ? 0.95 : 0.98;
                    double discountedPrice = car.getPrice()*coefficient;
                    return car.getName() + " " + car.getYear() + " " + String.format("%.0f", discountedPrice);
                  }).collect (Collectors.toList());

            Files.write (Path.of("HW28/data/car_price2.txt"), carsNewPrice);
            */

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
