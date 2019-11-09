package com.intruder.gdalexample;

import org.gdal.gdal.gdal;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Field;

public class GdalSetting {

    private static Logger logger = LoggerFactory.getLogger(GdalSetting.class);

    public static void setPath(){
        Boolean sw = false;
        try {
            ClassPathResource resource = new ClassPathResource("gdal24");
            String resourceAbsPath = resource.getURI().getPath();
            resourceAbsPath = resourceAbsPath.substring(1, resourceAbsPath.length() );
            resourceAbsPath = resourceAbsPath.replace("/", "\\");

            String current = printLibPath();
            String gdalLib = resourceAbsPath.replace("\\gdal24" , "");
            System.setProperty("java.library.path", current + ";" +gdalLib);

            try {
                Field fieldSysPath = ClassLoader.class.getDeclaredField("sys_paths");
                fieldSysPath.setAccessible( true );
                fieldSysPath.set( null, null );
            } catch (NoSuchFieldException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }


            String gdal_bin = resourceAbsPath + "\\bin";
            Boolean gdal_bin_sw = false;
            String gdal_data = resourceAbsPath + "\\bin\\gdal-data";
            Boolean gdal_data_sw = false;
            String gdal_driver_path = resourceAbsPath + "\\bin\\gdal\\plugins";
            Boolean gdal_driver_sw = false;
            String gdal_path = resourceAbsPath + "\\bin;";
            Boolean path_sw = false;

            String path = "";
            ProcessBuilder pb = new ProcessBuilder("CMD", "/C", "reg query \"HKEY_CURRENT_USER\\Environment\"");
            Process p = pb.start();
            BufferedReader reader =
                    new BufferedReader(new InputStreamReader(p.getInputStream()));
            StringBuilder builder = new StringBuilder();
            String line = null;
            while ( (line = reader.readLine()) != null) {
                builder.append(line);
                builder.append(System.getProperty("line.separator"));
                if (line.contains(gdal_bin)){
                    gdal_bin_sw = true;
                }
                if (line.contains(gdal_data)) {
                    gdal_data_sw = true;
                }
                if (line.contains(gdal_driver_path)){
                    gdal_driver_sw = true;
                }
                if (line.contains(gdal_path)){
                    path_sw = true;
                }
                if (line.contains("Path")){
                    String[] sp = line.split("    ");
                    path = sp[3];
                }
            }

            try {
                if (gdal_bin_sw == false) {
                    pb = new ProcessBuilder("CMD", "/C", "setx", "GDAL_BIN", gdal_bin);
                    p = pb.start();
                    p.waitFor();
                    logger.info("SET GDAL_BIN");
                }
                if (gdal_data_sw == false){
                    pb = new ProcessBuilder("CMD", "/C", "setx", "GDAL_DATA", gdal_data);
                    p = pb.start();
                    p.waitFor();
                    logger.info("SET GDAL_DATA");
                }
                if (gdal_driver_sw == false) {
                    pb = new ProcessBuilder("CMD", "/C", "setx", "GDAL_DRIVER_PATH", gdal_driver_path);
                    p = pb.start();
                    p.waitFor();
                    logger.info("SET GDAL_DRIVER_PATH");
                }
                if (path_sw == false){
                    pb = new ProcessBuilder("CMD", "/C", "setx", "path", path + gdal_path);
                    p = pb.start();
                    p.waitFor();
                    logger.info("SET path");
                }

            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            gdal.AllRegister();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static String printLibPath() {
        String current = System.getProperty("java.library.path");
        return current;
    }

}
