import java.io.*;
import java.nio.file.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;

public class Ds_01
{
    public static void main(String[] args)
    {
        try (Scanner iScanner = new Scanner(System.in, "Cp866"))
        {
            System.out.println("Введите данные (через пробел) в следующем формате: Фамилия Имя Отчество; Дата рождения (dd.MM.yyyy); Номер телефона (число); Пол (m или f):");
            String input = iScanner.nextLine();
            String[] inputData = input.split(" ");

            try
            {
                processUserData(inputData);
                System.out.println("Данные успешно записаны.");
            }
            catch (IllegalArgumentException e)
            {
                System.out.println(e.getMessage());
            }
            catch (IOException e)
            {
                System.out.println("Проблема с чтением или записью в файл: " + e.getMessage());
            }
        }
    }

    public static void processUserData(String[] inputData) throws IllegalArgumentException, IOException
    {
        if (inputData.length != 6)
        {
            throw new IllegalArgumentException("Вы ввели " + (inputData.length < 6 ? "меньше" : "больше") + " данных, чем требуется.");
        }

        String surname = inputData[0];
        String name = inputData[1];
        String patronymic = inputData[2];
        String birthDateString = inputData[3];
        String phoneNumberString = inputData[4];
        String genderString = inputData[5];

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
        dateFormat.setLenient(false);
        Date birthDate;
        long phoneNumber;
        String gender;

        try
        {
            birthDate = dateFormat.parse(birthDateString);
        }
        catch (ParseException e)
        {
            throw new IllegalArgumentException("Неверный формат даты рождения: " + birthDateString, e);
        }

        try
        {
            phoneNumber = Long.parseLong(phoneNumberString);
            if (phoneNumber < 0)
            {
                throw new NumberFormatException();
            }
        } 
        catch (NumberFormatException e)
        {
            throw new IllegalArgumentException("Неверный формат номера телефона: " + phoneNumberString, e);
        }

        if (genderString.length() == 1 && (genderString.equals("f") || genderString.equals("m")))
        {
            gender = genderString;
        }
        else
        {
            throw new IllegalArgumentException("Неверный формат пола: " + genderString);
        }

        Path filePath;
        try
        {
            filePath = Paths.get(surname + ".txt");
        }
        catch (InvalidPathException e)
        {
            throw new IllegalArgumentException("Недопустимые символы в фамилии: " + surname, e);
        }

        String output = String.format("<%s><%s><%s><%s><%d><%s>", surname, name, patronymic, birthDateString, phoneNumber, gender);
        try (BufferedWriter writer = Files.newBufferedWriter(filePath, StandardOpenOption.CREATE, StandardOpenOption.APPEND))
        {
            writer.write(output);
            writer.newLine();
        }
        catch (IOException e)
        {
            throw new IOException("Проблема с чтением или записью в файл.", e);
        }
    }
}