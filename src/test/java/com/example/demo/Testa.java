package com.example.demo;

import com.example.demo.task.FileMoveInTask;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;


@SpringBootTest
public class Testa {

    @Autowired
    private FileMoveInTask fileMoveInTask;
    @Test
    public void te(){
        fileMoveInTask.moveIn();
    }
}
