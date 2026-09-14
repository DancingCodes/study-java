package com.study.scheduledemo.controller;

import com.study.scheduledemo.common.Result;
import com.study.scheduledemo.task.ProductTask;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/task")
public class TaskController {

    @Autowired
    private ProductTask productTask;

    @GetMapping("/run")
    public Result<String> run() {
        String result = productTask.runManually();
        return Result.success(result);
    }
}
