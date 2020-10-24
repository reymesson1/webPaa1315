/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.example.demoexcel32.service;


import com.example.demoexcel32.controller.UploadController;
import com.example.demoexcel32.model.Answer;
import com.example.demoexcel32.model.Exam;
import com.example.demoexcel32.model.Qualification;
import com.example.demoexcel32.model.Question;
import com.example.demoexcel32.entity.FaceEntity;
import java.awt.Color;
import java.awt.image.BufferedImage;
import java.awt.image.DataBuffer;
import javax.imageio.ImageIO;
import java.io.File;



import org.opencv.core.*;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.objdetect.CascadeClassifier;

import static java.lang.System.loadLibrary;
import static org.opencv.imgproc.Imgproc.rectangle;


import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

@Service
public class UploadService {

    MasterService service;
    private List<FaceEntity> faceEntities;
    private Mat image;

    
    public UploadService(MasterService service){
        
        this.service=service;
    }
    
    @CrossOrigin(origins="http://localhost:4200")
    @RequestMapping(value = "/uploadtask", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public void uploadFile(@RequestParam("file") MultipartFile file) throws IOException {

        Path filepath = Paths.get("C:\\folderA\\", file.getOriginalFilename());

        try (OutputStream os = Files.newOutputStream(filepath)) {
            os.write(file.getBytes());
        }        
        System.out.println(file.getOriginalFilename());
    }
    
    @CrossOrigin(origins="http://localhost:4200")
    @RequestMapping(value = "/uploadexcel", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public void uploadFileExcel(@RequestParam("file") MultipartFile file) throws IOException {
            
            Path filepath = Paths.get("C:\\folderA\\", file.getOriginalFilename());

            try (OutputStream os = Files.newOutputStream(filepath)) {
                os.write(file.getBytes());
            }        

            File files = new File("C:/folderA/"+file.getOriginalFilename());

            Workbook workbook = WorkbookFactory.create(files);

            Sheet sheet = workbook.getSheetAt(0);
            
            Stream<Row> rowStream = StreamSupport.stream(sheet.spliterator(), false);

            rowStream.forEach(row->{

                    Stream<Cell> cellStream = StreamSupport.stream(row.spliterator(),false);
                    List<String> cellVals =	cellStream.map(cell->{
                            String cellVal = cell.getStringCellValue();
                            return cellVal;
                    })
                    .collect(Collectors.toList());

                    System.out.println(cellVals);
                    System.out.println(cellVals.get(0));

                    Qualification q = new Qualification();
                    q.setId("1");
                    q.setActividad(cellVals.get(0));
                    q.setCalificacion(cellVals.get(1));
                    q.setStudent("dddsd12313ffd");
                    q.setCreator("132klj23ljdsd");
                    service.qualifications.add(q);

            });

            System.out.println(file.getOriginalFilename());

    }

    public void uploadFileExcelExams(@RequestParam("file") MultipartFile file) throws IOException {

        Path filepath = Paths.get("C:\\folderA\\", file.getOriginalFilename());

        try (OutputStream os = Files.newOutputStream(filepath)) {
            os.write(file.getBytes());
        }        

        File files = new File("C:/folderA/"+file.getOriginalFilename());

        Workbook workbook = WorkbookFactory.create(files);

        Sheet sheet = workbook.getSheetAt(0);

        Stream<Row> rowStream = StreamSupport.stream(sheet.spliterator(), false);

        Exam e = new Exam();                        
        e.setId("1");
        e.setName("Exam 1");

        rowStream.forEach(row->{

                Stream<Cell> cellStream = StreamSupport.stream(row.spliterator(),false);
                List<String> cellVals =	cellStream.map(cell->{
                        String cellVal = cell.getStringCellValue();
                        return cellVal;
                })
                .collect(Collectors.toList());

                System.out.println(cellVals);
                System.out.println(cellVals.get(0));

//                              Question #1                                            
                            Question q = new Question();
                            q.setId("1");
                            q.setName(cellVals.get(0));
                                List<Answer> answers = new ArrayList<Answer>();
                                Answer a = new Answer();
                                a.setId("1");
                                a.setName(cellVals.get(1));
                                Answer a2 = new Answer();
                                a2.setId("2");
                                a2.setName(cellVals.get(2));
                                Answer a3 = new Answer();
                                a3.setId("4");
                                a3.setName(cellVals.get(3));
                                Answer a4 = new Answer();
                                a4.setId("4");
                                a4.setName(cellVals.get(4));
                                answers.add(a);
                                answers.add(a2);
                                answers.add(a3);
                                answers.add(a4);
                            q.setAnswers(answers);
                        service.questions.add(q);

        });

        e.setQuestions(service.questions);
        service.exams.add(e);

        System.out.println(file.getOriginalFilename());

    }
    
    @CrossOrigin(origins="http://localhost:4200")
    @RequestMapping(value = "/uploadimage", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public void uploadFileImage(@RequestParam("file") MultipartFile file) throws IOException {
        
            float percentage = 0;

         BufferedImage biA = ImageIO.read(new File("C:\\Images\\test1.jpg"));
         DataBuffer dbA = biA.getData().getDataBuffer();
         int sizeA = dbA.getSize();

         BufferedImage biB = ImageIO.read(new File("C:\\Images\\test2.jpg"));
         DataBuffer dbB = biB.getData().getDataBuffer();
         int sizeB = dbB.getSize();

        int count = 0;
        // compare data-buffer objects //
        if (sizeA == sizeB) {

            for (int i = 0; i < sizeA; i++) {

                if (dbA.getElem(i) == dbB.getElem(i)) {
                    count = count + 1;
                }

            }
            percentage = (count * 100) / sizeA;         
         
         System.out.println("Difference: "+percentage);
      }
//        return (String.format("Difference: %s ", percentage));
      


//         loadLibrary( Core.NATIVE_LIBRARY_NAME );
//
//         Mat image = Imgcodecs.imread(String.valueOf(file));
////         Mat image = Imgcodecs.imread("./src/main/resources/images/messivsronaldo.png");
//
//         CascadeClassifier faceDetector = new CascadeClassifier();
//         faceDetector.load("./src/main/resources/xml/haarcascade_frontalface_default.xml"); 
//
////         faceDetector.load("./src/main/resources/xml/haarcascade_frontalface_alt.xml"); 
////         faceDetector.load("./src/main/resources/xml/haarcascade_hand.xml"); 
//         
////         faceDetector.load("./src/main/resources/xml/lbpcascade_frontalface.xml"); 
//
//         MatOfRect faceDetections = new MatOfRect();
//        faceDetector.detectMultiScale(image, faceDetections);
//
//        System.out.println(String.format("Detected %s hands", faceDetections.toArray().length));
//        
//        for (Rect rect : faceDetections.toArray()) {
//            rectangle( image, new Point( rect.x, rect.y ), new Point( rect.width + rect.x, rect.height + rect.y ), new Scalar( 0, 255, 0 ) );
//        }
//        
////        Imgcodecs.imwrite( "./src/main/resources/images/messivsronaldo.png",image );
//        System.out.println("Hand Detected Successfully ");

//        return (String.format("Detected %s hands", faceDetections.toArray().length));
         
//         faceEntities=new ArrayList<>();
//         MatOfRect faceDetections = new MatOfRect();
//
//         
//         CascadeClassifier faceDetector = new CascadeClassifier();
//         faceDetector.load("./src/main/resources/xml/lbpcascade_frontalface.xml"); 
////         faceDetector.load("./src/main/resources/xml/haarcascade_frontalface_alt.xml"); 
//
////
//        image = Imgcodecs.imdecode(new MatOfByte(file.getBytes()), Imgcodecs.CV_LOAD_IMAGE_UNCHANGED);
//        faceDetector.detectMultiScale(image, faceDetections);
//        System.out.println(String.format("Detected %s faces", faceDetections.toArray().length));
//
//        return (String.format("Detected %s faces", faceDetections.toArray().length));

        
        
//         Mat image = Imgcodecs.imread(String.valueOf(file));
//         
//         MatOfRect faceDetections = new MatOfRect();
//        faceDetector.detectMultiScale( image, faceDetections );
//        
//        for (Rect rect : faceDetections.toArray()) {
//            rectangle( image, new Point( rect.x, rect.y ), new Point( rect.width + rect.x,
//                    rect.height + rect.y ), new Scalar( 0, 255, 0 ) );
//        }
//        
//        String filename = "Ouput.jpg";
//        System.out.println("Face Detected Successfully ");
//        Imgcodecs.imwrite( "c:\\folderA\\" + filename, image );
        
        
        
//            Path filepath = Paths.get("C:\\folderA\\", file.getOriginalFilename());

//            try (OutputStream os = Files.newOutputStream(filepath)) {
//                os.write(file.getBytes());
//            }        
//
//            File files = new File("C:/folderA/"+file.getOriginalFilename());
//
//            Workbook workbook = WorkbookFactory.create(files);
//
//            Sheet sheet = workbook.getSheetAt(0);
//            
//            Stream<Row> rowStream = StreamSupport.stream(sheet.spliterator(), false);
//
//            rowStream.forEach(row->{
//
//                    Stream<Cell> cellStream = StreamSupport.stream(row.spliterator(),false);
//                    List<String> cellVals =	cellStream.map(cell->{
//                            String cellVal = cell.getStringCellValue();
//                            return cellVal;
//                    })
//                    .collect(Collectors.toList());
//
//                    System.out.println(cellVals);
//                    System.out.println(cellVals.get(0));
//
//                    Qualification q = new Qualification();
//                    q.setId("1");
//                    q.setActividad(cellVals.get(0));
//                    q.setCalificacion(cellVals.get(1));
//                    q.setStudent("dddsd12313ffd");
//                    q.setCreator("132klj23ljdsd");
//                    service.qualifications.add(q);
//
//            });

//            System.out.println(file.getOriginalFilename());
//            System.out.println("uploadservice");

      }
    
    
    

}