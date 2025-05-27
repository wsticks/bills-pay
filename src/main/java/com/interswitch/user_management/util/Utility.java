package com.interswitch.user_management.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.sun.management.OperatingSystemMXBean;
import jakarta.servlet.http.HttpServletRequest;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.NetworkInterface;
import java.net.URL;
import java.nio.file.DirectoryStream;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.StandardCopyOption;
import java.nio.file.attribute.BasicFileAttributes;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;
import javax.imageio.ImageIO;
import org.apache.commons.lang3.StringUtils;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.validation.BindingResult;

public class Utility {

    private static final Logger LOGGER = LoggerFactory.getLogger(Utility.class);

    private static final ModelMapper MODEL_MAPPER = new ModelMapper();
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    public static void resize(String inputImagePath,
                              String outputImagePath, double percent) throws IOException {
        File inputFile = new File(inputImagePath);
        BufferedImage inputImage = ImageIO.read(inputFile);
        int scaledWidth = (int) (inputImage.getWidth() * percent);
        int scaledHeight = (int) (inputImage.getHeight() * percent);
        resize(inputImagePath, outputImagePath, scaledWidth, scaledHeight);
    }

    public static void resize(String inputImagePath,
                              String outputImagePath, int scaledWidth, int scaledHeight)
            throws IOException {
        // reads input image
        File inputFile = new File(inputImagePath);
        BufferedImage inputImage = ImageIO.read(inputFile);

        // creates output image
        BufferedImage outputImage = new BufferedImage(scaledWidth,
                scaledHeight, inputImage.getType());

        // scales the input image to the output image
        Graphics2D g2d = outputImage.createGraphics();
        g2d.drawImage(inputImage, 0, 0, scaledWidth, scaledHeight, null);
        g2d.dispose();

        // extracts extension of output file
        String formatName = outputImagePath.substring(outputImagePath
                .lastIndexOf(".") + 1);

        // writes to output file
        ImageIO.write(outputImage, formatName, new File(outputImagePath));
    }

    /**
     * Convert errors in binding result to a Map
     *
     * @param bindingResult
     * @return
     */
    public static Map<String, String> errors(BindingResult bindingResult) {
        Map<String, String> errors = new HashMap<>();
        bindingResult.getFieldErrors().forEach(err -> errors.put(err.getField(), err.getDefaultMessage()));
        return errors;
    }

    /**
     * Convert request Parameters in HttpServletRequest object to a map
     *
     * @param r
     * @return
     */
    public static Map<String, String> getRequestParams(HttpServletRequest r) {
        Map<String, String> params = new HashMap<>();
        Enumeration<String> e = r.getParameterNames();
        while (e.hasMoreElements()) {
            String name = e.nextElement();
            params.put(name, r.getParameter(name));
        }
        return params;
    }

    public static String generateClassName(String classLevelName, String classArm) throws IllegalArgumentException {
        if (StringUtils.isBlank(classLevelName) || StringUtils.isBlank(classArm)) {
            throw new IllegalArgumentException("class Type must not be null, classLevel cannot be zero and classArm cannot be empty");
        }

        StringBuilder sb = new StringBuilder(classLevelName.trim());
        sb.append(" ").append(classArm.trim());

        return sb.toString();
    }

    /**
     * Returns Gender pronoun as his/her
     *
     * @param gender
     * @return
     */
    public static String getGenderPronoun1(String gender) {

        if (StringUtils.isBlank(gender)) return "his";//default to male
        gender = gender.replaceAll("[\\s]+", "");
        return "F".equalsIgnoreCase(gender) || "Female".equalsIgnoreCase(gender) ? "her" : "his";

    }

    /**
     * Returns Gender pronoun as him/her
     *
     * @param gender
     * @return
     */
    public static String getGenderPronoun2(String gender) {

        if (StringUtils.isBlank(gender)) return "him";//default to male
        gender = gender.replaceAll("[\\s]+", "");
        return "F".equalsIgnoreCase(gender) || "Female".equalsIgnoreCase(gender) ? "her" : "him";
    }

    /**
     * Returns Gender pronoun as he/she
     *
     * @param gender
     * @return
     */
    public static String getGenderPronoun3(String gender) {
        if (StringUtils.isBlank(gender)) {
            return "he";//default to male
        }

        gender = gender.replaceAll("[\\s]+", "");

        if ("M".equalsIgnoreCase(gender) || "Male".equalsIgnoreCase(gender)) {
            return "he";
        }

        if ("F".equalsIgnoreCase(gender) || "Female".equalsIgnoreCase(gender)) {
            return "she";
        }

        return "he";

    }

    public static <T> T fromJson(String sourceJson, Class<T> targetClass) throws IOException {
        ObjectMapper objectMapper = getObjectMapper();
        T object = objectMapper.readValue(sourceJson, targetClass);
        return object;
    }

    public static ObjectMapper getObjectMapper(){
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        return objectMapper;
    }

    public static String createJsonStringFromObject(Object object) {
        ObjectWriter ow = getObjectMapper().writer().withDefaultPrettyPrinter();
        String json = "";
        try {
            json = ow.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            System.out.println(e.getMessage());
        }
        return json;
    }

    public static <T> T map(String json, Class<T> clazz) {
        try {
            return getObjectMapper().readValue(json, clazz);
        } catch (IOException e) {
            LOGGER.warn("Error occur during mapping {}", e.getMessage());
        }
        return newInstance(clazz);
    }

    public static String toJson(Object object) {
        return createJsonStringFromObject(object);
    }

    private static Path buildPath(final Path root, final Path child) {
        if (root == null) {
            return child;
        } else {
            return Paths.get(root.toString(), child.toString());
        }
    }

    /**
     * Writes a given directory or file to a zip file through a zipOutputStream
     *
     * @param out - The zipOutputStream that writes a file or folder to a zip
     * file
     * @param zipDir - the zip directory path to write to
     * @param dir - the regular directory or file to read from
     * @throws IOException
     */
    private static void addZipDir(final ZipOutputStream out, final Path zipDir, final Path dir) throws IOException {
        try (DirectoryStream<Path> stream = Files.newDirectoryStream(dir)) {
            for (Path child : stream) {
                Path entry = buildPath(zipDir, child.getFileName());
                if (Files.isDirectory(child)) {
                    addZipDir(out, entry, child);
                } else {
                    out.putNextEntry(new ZipEntry(entry.toString()));
                    Files.copy(child, out);
                    out.closeEntry();
                }
            }
        }
    }

    /**
     * Compress the given path to a zip
     *
     * @param path
     * @throws IOException
     */
    public static void zipDir(final Path path) throws IOException {
        if (!Files.isDirectory(path)) {
            throw new IllegalArgumentException("Path must be a directory.");
        }

        BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(path.toString() + ".zip"));

        try (ZipOutputStream out = new ZipOutputStream(bos)) {
            addZipDir(out, path.getFileName(), path);
        }
    }

    /**
     * Unzip a zip file
     *
     * @param zipFile input zip file
     * @param outputFolder regular file output folder/directory
     */
    public static void unZipIt(Path zipFile, Path outputFolder) {

        byte[] buffer = new byte[1024];

        //get the zip file content
        try (ZipInputStream zis = new ZipInputStream(new FileInputStream(zipFile.toString()))) {

            //creator output directory is not exists
            File folder = new File(outputFolder.toString());
            if (!folder.exists()) {
                folder.mkdir();
            }

            addFromZipToRootFolder(zis, folder, buffer);

        } catch (IOException ex) {
        }
    }

    /**
     * Add individual zip entry to a destination folder
     *
     * @param zis - ZipInpustream created from a compressed zip file
     * @param outputFolder - File to output zip entry to
     * @param buffer - buffer to hold intermittent file content
     * @throws IOException
     */
    private static void addFromZipToFolder(ZipInputStream zis, File outputFolder, byte[] buffer) throws IOException {
        if (zis == null) {
            return;
        }

        //get the zipped file list entry
        ZipEntry ze = zis.getNextEntry();

        while (ze != null) {

            String fileName = ze.getName();
            File newFile = new File(outputFolder + File.separator + fileName);

            System.out.println("file unzip : " + newFile.getAbsoluteFile());

            //creator all non exists folders
            //else you will hit FileNotFoundException for compressed folder
            new File(newFile.getParent()).mkdirs();

            try (FileOutputStream fos = new FileOutputStream(newFile)) {
                int len;
                while ((len = zis.read(buffer)) > 0) {
                    fos.write(buffer, 0, len);
                }
            }
            ze = zis.getNextEntry();
        }

        System.out.println("Done");
    }

    /**
     * Add individual zip entry to root folder
     *
     * @param zis - ZipInpustream created from a compressed zip file
     * @param outputFolder - File to output zip entry to
     * @param buffer - buffer to hold intermittent file content
     * @throws IOException
     */
    private static void addFromZipToRootFolder(ZipInputStream zis, File outputFolder, byte[] buffer) throws IOException {
        if (zis == null) {
            return;
        }

        //get the zipped file list entry
        ZipEntry ze = zis.getNextEntry();

        while (ze != null) {

            String fileName = ze.getName();
            String[] arr = fileName.split("/");
            fileName = arr[arr.length - 1];
            File newFile = new File(outputFolder + File.separator + fileName);

            System.out.println("file unzip : " + newFile.getAbsoluteFile());

            //creator all non exists folders
            //else you will hit FileNotFoundException for compressed folder
            new File(newFile.getParent()).mkdirs();

            try (FileOutputStream fos = new FileOutputStream(newFile)) {
                int len;
                while ((len = zis.read(buffer)) > 0) {
                    fos.write(buffer, 0, len);
                }
            }
            ze = zis.getNextEntry();
        }

        System.out.println("Done");

    }

    public static boolean isBcryptHashed(String password) {

        return StringUtils.isNotBlank(password) && password.length() == 60 && (password.startsWith("$2a$") || password.startsWith("$2b$") || password.startsWith("$2y$"));
    }

    public static void download(String url, String destinationFile) throws IOException {
        Path path = Paths.get(destinationFile);
        Files.createDirectories(path.getParent());
        Files.copy(new URL(url).openStream(), path, StandardCopyOption.REPLACE_EXISTING);
    }

    public static boolean isNetworkAvailable(String uri) {
        try {
            final URL url = new URL(uri);//will switch to studylabs domain when fully hosted
            final HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.connect();
            return conn.getResponseCode() == HttpURLConnection.HTTP_OK;

        } catch (MalformedURLException e) {
            //System.out.println("Malformed Url Exception");
            throw new RuntimeException(e);
        } catch (IOException e) {
            //System.out.println("Network is unavailable via exception");
            return false;
        }
    }

    //    public static boolean studylabCentralIsAvailable() {
//        return isNetworkAvailable(StudylabCentralApplication.APP_BASE_URL + "/api/isAlive");
//    }
//    public static boolean studylabCentralIsNotAvailable() {
//        return !studylabCentralIsAvailable();
//    }
    public static void listFilesInUploadDir() {
        Path extractDestination = Paths.get("uploads");

        try {
            Files.walkFileTree(extractDestination, new SimpleFileVisitor<Path>() {

                @Override
                public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) {
                    System.out.println("Visiting file: " + file.getFileName());

                    return FileVisitResult.CONTINUE;
                }

            });

        } catch (IOException ioe) {
            System.out.println("An Io exception happened, while walking through files ");
        }
        /* System.out.println("FileName: " + f.getFileName());
        });*/
    }

    public static Optional<String> formatPhone(String phone) {
        String formattedPhone;
        if (StringUtils.isBlank(phone)) return Optional.empty();
        phone = phone.trim().replaceAll(" ", "").replaceAll("-", "");
        if (phone.startsWith("+234")) formattedPhone = phone.substring(1);
        else if (phone.startsWith("234")) formattedPhone = phone;
        else if (phone.startsWith("0")) formattedPhone = phone.replaceFirst("0", "234");
        else formattedPhone = "234" + phone;

        if (null != formattedPhone && formattedPhone.length() != 13) {
            throw new IllegalArgumentException("Formatted phoneNumber number must be 13 characters");
        }

        return Optional.ofNullable(formattedPhone);
    }

    public static String getFileExtension(String fileName) {
        if (StringUtils.isBlank(fileName)) throw new IllegalArgumentException("File name should not be null");
        int i = fileName.lastIndexOf('.');
        return (i > 0) ? fileName.substring(i + 1) : "";
    }

    public static <T> T map(Object source, Class<T> destinationType) throws IllegalArgumentException {
        return source != null ? MODEL_MAPPER.map(source, destinationType) : null;
    }

    public static <T, S> Optional<T> mapOptional(Optional<S> source, Class<T> destinationType) throws IllegalArgumentException {
        return source.map(o -> MODEL_MAPPER.map(o, destinationType));
    }

    public static <T, R> List<T> map(List<R> sourceList, Class<T> clazz) throws IllegalArgumentException {
        if (sourceList == null) return Collections.emptyList();
        return sourceList.stream().map(x -> map(x, clazz)).collect(Collectors.toList());
    }

    public static <T, R> Set<T> map(Set<R> sourceList, Class<T> clazz) throws IllegalArgumentException {
        if (sourceList == null) return Collections.emptySet();
        return sourceList.stream().map(x -> map(x, clazz)).collect(Collectors.toSet());
    }

    public static <T> List<T> filterObjects(List<T> objects, Predicate<T> predicate) {
        return objects.stream().filter(predicate).collect(Collectors.toList());
    }

    public static <T, S> List<S> filterAndMapObject(List<T> objects, Predicate<T> predicate, Class<S> destination) {
        return map(objects.stream().filter(predicate).collect(Collectors.toList()), destination);
    }

    public static <T, S> List<S> filterAndMapObject(List<T> objects, Predicate<T> predicate, Function<? super T, S> mapper) {
        return objects.stream().filter(predicate).map(mapper).collect(Collectors.toList());
    }

    public static <S,T> Page<T> map(Page<S> page, Class<T> clazz) {
        if (page == null) throw new IllegalArgumentException("Page object can not be null");
        return page.map(x -> map(x,clazz));
    }

    public static <T, ID> T newInstanceWithId(Class<T> model, ID id) {
        if (id == null) return null;
        T object = newInstance(model);
        setId(object, model, id);
        return object;
    }

    public static <T> T newInstance(Class<T> model) {
        try {
            Constructor<T> constructor = model.getConstructor();
            constructor.setAccessible(true);
            return constructor.newInstance();
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException | InstantiationException e) {
            throw new IllegalArgumentException("Could not instantiate class " + model.getName());
        }
    }

    public static <T, ID> List<T> getEntitiesFromIds(Class<T> model, List<ID> ids) {
        return ids == null ? new ArrayList<>() : ids.stream().map(id -> newInstanceWithId(model, id)).collect(Collectors.toList());
    }

    public static <T, ID> Set<T> getEntities(Class<T> model, List<ID> ids) {
        return ids == null ? new HashSet<>() : ids.stream().map(id -> newInstanceWithId(model, id)).collect(Collectors.toSet());
    }

    public static <T, ID> T setId(T object, Class<T> model, ID id) {
        Class clazz = id instanceof Long ? Long.class : String.class;
        try {
            Method s = model.getMethod("setId", clazz);
            s.invoke(object, id);
        } catch (SecurityException | NoSuchMethodException | IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
            LOGGER.warn("Error while setting id for {}", model.getSimpleName());
        }
        return object;
    }

    public static String getIpAddress() throws Exception {
        return InetAddress.getLocalHost().getHostAddress();
    }

    public static String getMcAddress() throws Exception {
        byte[] mac = NetworkInterface.getByInetAddress(InetAddress.getLocalHost()).getHardwareAddress();
        return IntStream
                .range(0, mac.length)
                .mapToObj(i -> String.format("%02X%s", mac[i], (i < mac.length - 1) ? "-" : ""))
                .collect(Collectors.joining());
    }

    public static double getProcessCpuLoad() {
        double cpuUsage = ((OperatingSystemMXBean) ManagementFactory.getOperatingSystemMXBean()).getProcessCpuLoad();
        return ((int)(cpuUsage * 1000))/10.0;
    }

    public static double getSystemCpuLoad() {
        double cpuUsage = ((OperatingSystemMXBean) ManagementFactory.getOperatingSystemMXBean()).getSystemCpuLoad();
        return ((int)(cpuUsage * 1000))/10.0;
    }

    public static long getTotalPhysicalMemorySize() {
        return ((OperatingSystemMXBean) ManagementFactory.getOperatingSystemMXBean()).getTotalPhysicalMemorySize();
    }

    public static long getFreePhysicalMemorySize() {
        return ((OperatingSystemMXBean) ManagementFactory.getOperatingSystemMXBean()).getFreePhysicalMemorySize();
    }

    public static Date getDateFromString(String date) {
        LocalDateTime parsedDate = toLocalDateTime(date);
        if (parsedDate == null) return null;
        return Date.from(parsedDate.atZone(ZoneId.systemDefault()).toInstant());
    }

    public static LocalDateTime toLocalDateTime(String date) {
        if (StringUtils.isEmpty(date)) return null;
        return LocalDate.parse(date, DateTimeFormatter.ofPattern("dd/MM/yyyy")).atStartOfDay();
    }

  public static String trimValue(String value) {
    if (value != null) {
      return value.trim();
    }
    return null;
  }
}
