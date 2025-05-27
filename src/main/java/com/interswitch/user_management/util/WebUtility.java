package com.interswitch.user_management.util;

import com.interswitch.user_management.exception.CustomException;
import jakarta.servlet.http.HttpServletRequest;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.text.Normalizer;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

@Component
public class WebUtility {

    private static final PasswordEncoder PASSWORD_ENCODER = new BCryptPasswordEncoder();
    private static final Pattern NONLATIN = Pattern.compile("[^\\w_-]");
    private static final Pattern SEPARATORS = Pattern.compile("[\\s\\p{Punct}&&[^-]]");
    private static final Pattern DATA_URL_PATTERN = Pattern.compile("^data:image/(.+?);base64,\\s*", Pattern.CASE_INSENSITIVE);

    public static String hashPassword(String password) {
        return PASSWORD_ENCODER.encode(password);
    }

    public static boolean confirmPasswordIsCorrect(String password, String hashedPassword) {
        return PASSWORD_ENCODER.matches(password, hashedPassword);
    }

    public static boolean confirmMatchingPassword(String password, String matchingPassword) {
        return password.equals(matchingPassword);
    }

    public static boolean isBcryptHashed(String password) {
        return StringUtils.isNotBlank(password) && password.length() == 60 && (password.startsWith("$2a$") || password.startsWith("$2b$") || password.startsWith("$2y$"));
    }

    public static String encode(String password) {
        return PASSWORD_ENCODER.encode(password);
    }

    public static String generateNipSessionId(final String nipCode) {
        return nipCode + DateUtils
                .getDateAsString2(new Date()) + generateRandomBoundedLong(100000000000L, 999999999999L);
    }

    public static String generateShortSessionId() {
        String randomString = DateUtils
                .getDateAsString2(new Date()) + generateRandomBoundedLong(100000000000L, 999999999999L);
        return randomString.substring(5,17);
    }

    public static String generateShortNipSessionId(final String nipCode) {
        return nipCode + DateUtils.getDateAsString7(new Date()) + generateRandomBoundedLong(10000L, 99999L);
    }


    public static String makeSlug(String input) {
        String noseparators = SEPARATORS.matcher(input).replaceAll("-");
        String normalized = Normalizer.normalize(noseparators, Normalizer.Form.NFD);
        String slug = NONLATIN.matcher(normalized).replaceAll("");
        return slug.toLowerCase(Locale.ENGLISH).replaceAll("-{2,}","-").replaceAll("^-|-$","");
    }

    public static String makeUnderscore(String input) {
        String noseparators = SEPARATORS.matcher(input).replaceAll("_");
        String normalized = Normalizer.normalize(noseparators, Normalizer.Form.NFD);
        String underscore = NONLATIN.matcher(normalized).replaceAll("");
        return underscore.toUpperCase(Locale.ENGLISH).replaceAll("-{2,}","_").replaceAll("^-|-$","");
    }

    public static Pageable createPageRequest(CustomPageable customPageable) {
        Sort sort;
        if (customPageable.getSortDirection().equalsIgnoreCase("desc")){
            sort = Sort.by(Sort.Direction.DESC, customPageable.getSortBy());
        } else {
            sort = Sort.by(Sort.Direction.ASC, customPageable.getSortBy());
        }
        return PageRequest.of(customPageable.getPage(), customPageable.getSize(), sort);
    }

    public static void writemBase64ImageToFile(String encodedImg, String fileName) throws IOException {
        byte[] decodedImg = Base64.getMimeDecoder().decode(encodedImg.split(",")[1]);
        String uploadRootPath = "upload";
        File uploadRootDir = new File(uploadRootPath);
        // Create directory if it not exists.
        if (!uploadRootDir.exists()) {
            uploadRootDir.mkdirs();
        }
        Path destinationFile = Paths.get(uploadRootDir.getAbsolutePath(), fileName);
        Files.write(destinationFile, decodedImg);
    }

    public static void uploadFileImage(MultipartFile fileData) throws IOException {
        String uploadRootPath = "upload";
        File uploadRootDir = new File(uploadRootPath);
        // Create directory if it not exists.
        if (!uploadRootDir.exists()) {
            uploadRootDir.mkdirs();
        }
        String name = fileData.getOriginalFilename();
        long fsize = fileData.getSize();
        String filePath = new File(fileData.getOriginalFilename()).getAbsolutePath();

        if (name != null && name.length() > 0) {
            if(fsize > 1048576){
                throw new CustomException("Oops! Image size is too large. Try with a smaller size");
            }
            try {
                try {
                    System.out.println("before resize");
                    Utility.resize(filePath,uploadRootDir.getAbsolutePath() + File.separator + name,50);
                    System.out.println("after resize");
                } catch (IOException e) {
                    e.printStackTrace();
                }
                // Create the file at server
                File serverFile = new File(uploadRootDir.getAbsolutePath() + File.separator + name);

                BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(serverFile));
                stream.write(fileData.getBytes());
                stream.close();
            } catch (IOException e) {
                System.out.println("Error Write file: " + name);
            }
        }
    }

    // Returns the image in a byte array
    public static byte[] readImage(String imageName) throws IOException {
        String uploadRootPath = "upload";
        File uploadRootDir = new File(uploadRootPath);
        File imageFile = new File(uploadRootDir.getAbsolutePath() + File.separator + imageName);
        byte[] fileContent = FileUtils.readFileToByteArray(imageFile);

        return fileContent;
    }

    // Returns the image in a byte array
    public static byte[] readPdf(String fileName) throws IOException {
        String uploadRootPath = "upload/pdf";
        File uploadRootDir = new File(uploadRootPath);
        File imageFile = new File(uploadRootDir.getAbsolutePath() + File.separator + fileName);
        byte[] fileContent = FileUtils.readFileToByteArray(imageFile);

        return fileContent;
    }

    public static long generateRandomBoundedLong(long min, long max) {
        return min + (long) (Math.random() * (max - min));
    }

    public static int generateRandomIntegerBetweenRange(int min, int max) {
        Random r = new Random();
        return r.nextInt((max - min) + 1) + min;
    }

    public static String generateRandomAlphaNumericString(int length){
        boolean useLetters = true;
        boolean useNumbers = true;
        return RandomStringUtils.random(length, useLetters, useNumbers);
    }

    public static String generateRandomNumericString(int length){
        boolean useLetters = false;
        boolean useNumbers = true;
        return RandomStringUtils.random(length, useLetters, useNumbers);
    }

    public static String generateNuban( String cbnCode) {
        String num =  shuffle(padLeft(String.valueOf(generateRandomIntegerBetweenRange(1, 99999999)), 8));

        return num + String.valueOf(checksum(cbnCode, num));
    }

    public static int checksum(String cbnCode, String serial) {
        if (serial.length() != 9) {
            System.out.println("The serial must be 9 digits");
            return -1;
        } else {
            String newSerial = cbnCode + serial;
            int num = 0;
            int c = 0;
            int p = 0;
            for (int i = 0; i < 4; i++) {
                int j = Integer.parseInt(String.valueOf(newSerial.charAt(c)));
                p = j * 3;
                num = num + p;

                c++;
                j = Integer.parseInt(String.valueOf(newSerial.charAt(c)));
                p = j * 7;
                num = num + p;

                c++;
                j = Integer.parseInt(String.valueOf(newSerial.charAt(c)));
                p = j * 3;
                num = num + p;

                c++;
            }
            num = num % 10;
            System.out.println("Modulo: " + num);
            num = 10 - num;

            return num;
        }
    }

    public static String reverseString(String input) {
        StringBuilder input1 = new StringBuilder();
        input1.append(input);
        input1 = input1.reverse();

        return input1.toString();
    }

    public static String padLeft(String msg, int size) {
        return StringUtils.leftPad(msg, size, "0");
    }

    public static String padLeft(String msg, String padStr, int size) {
        return StringUtils.leftPad(msg, size, padStr);
    }

    public static String shuffle(String input){
        List<Character> characters = new ArrayList<Character>();
        for(char c:input.toCharArray()){
            characters.add(c);
        }
        StringBuilder output = new StringBuilder(input.length());
        while(characters.size()!=0){
            int randPicker = (int)(Math.random()*characters.size());
            output.append(characters.remove(randPicker));
        }
        return output.toString();
    }

    public static String getClientIP(HttpServletRequest request) {
        String remoteAddr = "";

        if (request != null) {
            remoteAddr = request.getHeader("X-FORWARDED-FOR");
            if (remoteAddr == null || "".equals(remoteAddr)) {
                remoteAddr = request.getRemoteAddr();
            }
        }

        if (remoteAddr.indexOf(":") > 0) {
            remoteAddr = remoteAddr.substring(0, remoteAddr.indexOf(":"));
        }

        return remoteAddr;
    }

    public static  <T> CompletableFuture<List<T>> allOf(List<CompletableFuture<T>> futuresList) {
        CompletableFuture<Void> allFuturesResult =
                CompletableFuture.allOf(futuresList.toArray(new CompletableFuture[futuresList.size()]));
        return allFuturesResult.thenApply(v ->
                futuresList.stream().
                        map(future -> future.join()).
                        collect(Collectors.<T>toList())
        );
    }

    public static boolean isBase64Image(String encodedImage) {
        if (encodedImage.startsWith("data:")) {
            final Matcher m = DATA_URL_PATTERN.matcher(encodedImage);
            if (m.find()) {
                return true;
            } else {
                return false;
            }
        }
        return false;
    }

    public static String uploadFile(String fileName, MultipartFile multipartFile)
            throws IOException {
        Path uploadPath = Paths.get("upload/pdf");

        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        String fileCode = generateRandomAlphaNumericString(8);

        try (InputStream inputStream = multipartFile.getInputStream()) {
            Path filePath = uploadPath.resolve(fileCode + "-" + fileName);
            Files.copy(inputStream, filePath, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException ioe) {
            throw new IOException("Could not save file: " + fileName, ioe);
        }

        return fileCode;
    }

    public static Resource getFileAsResource(String fileCode) throws IOException {
        final List<Path> foundFiles = new ArrayList<>();
        Path dirPath = Paths.get("upload/pdf");

        Files.list(dirPath).forEach(file -> {
            if (file.getFileName().toString().startsWith(fileCode)) {
                foundFiles.add(file);
                return;
            }
        });

        if (!foundFiles.isEmpty()) {
            return new UrlResource(foundFiles.get(0).toUri());
        }

        return null;
    }

    public static String generateSessionId() {
        return DateUtils.getDateAsString7(new Date()) + generateRandomBoundedLong(100000L, 999999L);
    }

    public static Object getLastElement(final Collection c) {
        final Iterator itr = c.iterator();
        Object lastElement = itr.next();
        while(itr.hasNext()) {
            lastElement = itr.next();
        }
        return lastElement;
    }

    public static String truncateNarration(String narration) {
        if (narration.length() > 35) {
            return String.format("%s...", narration.substring(0, 36));
        }
        return narration;
    }

    /**
     * Turn the first letters of every word to an upper-case character
     * @param str
     * @return str
     */
    public static String capitalizeWord(String str){
        String words[];
        try {
            words = str.split("\\s");
        } catch (Exception e) {
            words = new String[1];
            words[0] = str;
        }

        String capitalizeWord="";
        for(String w:words){
            String first=w.substring(0,1);
            String afterfirst=w.substring(1);
            capitalizeWord+=first.toUpperCase()+afterfirst+" ";
        }
        return capitalizeWord.trim();
    }

    public static String pciMask(String unmaskedValue, int keepLastDigits) {
        if (StringUtils.isNotBlank(unmaskedValue)) {
            if (keepLastDigits < unmaskedValue.length()) {
                String kept = unmaskedValue.substring(unmaskedValue.length() - keepLastDigits);

                return padLeft(kept, "*", unmaskedValue.length());
            }
        }

        return unmaskedValue;
    }

    public static String piiMask(String unmaskedValue, int keepLastDigits) {
        if (StringUtils.isBlank(unmaskedValue)) {
            return unmaskedValue;
        }
        if (keepLastDigits < unmaskedValue.length()) {
            String kept = unmaskedValue.substring(unmaskedValue.length() - keepLastDigits);

            return generateRandomAlphaNumericString(unmaskedValue.length() - keepLastDigits) + kept;
        }

        return unmaskedValue;
    }

    public static String maskNumber(String creditCardNumber) {
        final String s = creditCardNumber.replaceAll("\\D", "");

        final int start = 4;
        final int end = s.length() - 4;
        final String overlay = StringUtils.repeat("*", end - start);

        return StringUtils.overlay(s, overlay, start, end);
    }
}
