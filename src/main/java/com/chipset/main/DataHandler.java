package com.chipset.main;

import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;
import com.opencsv.exceptions.CsvValidationException;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DataHandler {
    public static boolean fileExists(String filePath) {
        File temp = new File(filePath);
        return temp.exists();
    }

    public static void createCSV(Guild guild) {
        String path = "src/main/resources/data/"+guild.getName()+"-members.csv";
        File file = new File(path);
        try {
            FileWriter outputFile = new FileWriter(file);
            CSVWriter writer = new CSVWriter(outputFile);

            //write header
            String[] header = {"Name", "ID", "Roles", "Insured", "GE", "GE_Multi", "GE_Posted", "GE_First_Time"};
            writer.writeNext(header);

            //get data
            List<Member> memberList = new ArrayList<>();
            guild.loadMembers(memberList::add).get();

            //write data
            for (Member member : memberList) {
                String[] data = {
                        member.getEffectiveName(),
                        member.getId(),
                        String.valueOf(member.getRoles()),
                        "false",
                        "false",
                        "1",
                        "false",
                        "true"
                }; // set user data

                writer.writeNext(data); // append user data
            }
            //close writer
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void createCSV(Guild guild, List<List<String>> storedData) {
        String path = "src/main/resources/data/"+guild.getName()+"-members.csv";
        File file = new File(path);
        try {
            FileWriter outputFile = new FileWriter(file);
            CSVWriter writer = new CSVWriter(outputFile);

            //write header
            String[] header = {"Name", "ID", "Roles", "Insured", "GE", "GE_Multi", "GE_Posted", "GE_First_Time"};
            writer.writeNext(header);

            //get data
            List<Member> memberList = new ArrayList<>();
            guild.loadMembers(memberList::add).get();

            //write data
            for (int i=0; i< memberList.size(); i++) {
                List<String> d;
                try {
                    d = storedData.get(i + 1); // get same row from stored data
                } catch (IndexOutOfBoundsException e) {
                    d = List.of("", "", "", "false", "", "", "false", "true");
                }
                String[] data = {
                        memberList.get(i).getEffectiveName(),
                        memberList.get(i).getId(),
                        String.valueOf(memberList.get(i).getRoles()),
                        d.get(3),
                        "false",
                        "1",
                        d.get(6),
                        d.get(7)
                }; // set user data

                writer.writeNext(data); // append user data
            }
            //close writer
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void updateCSV(Guild guild) {
        String dataPath = "src/main/resources/data/";
        if (fileExists(dataPath+guild.getName()+"-members.csv")) {
            // read DB
            File file = new File(dataPath+guild.getName()+"-members.csv");
            List<List<String>> data = new ArrayList<>();
            try {
                FileReader reader = new FileReader(file);
                CSVReader csvReader = new CSVReader(reader);
                String[] next;

                while ((next = csvReader.readNext()) != null) {
                    //data.addAll(Arrays.asList(next)); // save data currently stored in DB
                    List<String> d = new ArrayList<>(Arrays.asList(next));
                    data.add(d);
                }
                createCSV(guild, data);// update DB
            } catch (CsvValidationException | IOException e) {
                e.printStackTrace();
            }
        } else {
            // create DB
            createCSV(guild);
            System.out.println("DB created for "+guild.getName());
        }
    }

    public static List<List<String>> getCSV(Guild guild) {
        String dataPath = "src/main/resources/data/";
        List<List<String>> res = null;
        if (fileExists(dataPath+guild.getName()+"-members.csv")) {
            // read DB
            File file = new File(dataPath+guild.getName()+"-members.csv");
            List<List<String>> data = new ArrayList<>();
            try {
                FileReader reader = new FileReader(file);
                CSVReader csvReader = new CSVReader(reader);
                String[] next;

                while ((next = csvReader.readNext()) != null) {
                    //data.addAll(Arrays.asList(next)); // save data currently stored in DB
                    List<String> d = new ArrayList<>(Arrays.asList(next));
                    data.add(d);
                }
                res = data;
            } catch (CsvValidationException | IOException e) {
                e.printStackTrace();
            }
        } else {
            // create DB
            createCSV(guild);
            getCSV(guild); //re-run to get data
        }
        return res;
    }

    public static void updateCSV(Guild guild, String id, int index, String val) {
        // updates a certain value in a CSV
        List<List<String>> dt = getCSV(guild);
        for (List<String> user : dt) {
            if (user.get(1).equals(id)) {
                user.set(index, val);
            }
        }
        //write data to DB
        createCSV(guild, dt);
    }
}
