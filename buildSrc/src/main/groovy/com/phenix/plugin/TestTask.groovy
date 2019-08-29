package com.phenix.plugin

import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction

/**
 * @author ：zhouphenix
 * @date ：Created in 2019/8/19 9:44
 * @description ：task自定义测试
 * @modified By：zhouphenix
 * @version: V1.0
 */
class TestTask extends DefaultTask {

    String message
    String recipient


    @TaskAction
    void testGreeting() {
        println "${message}, ${recipient}! ---${project.extPhenix.name} : ${project['extPhenix'].desc}"
    }




}

