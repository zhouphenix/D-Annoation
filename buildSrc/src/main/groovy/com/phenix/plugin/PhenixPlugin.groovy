package com.phenix.plugin

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.Task

/**
 * @author ：zhouphenix
 * @date ：Created in 2019/8/16 14:49
 * @description ：${description}*
 * @modified By：TODO
 * @version: $version$
 */
class PhenixPlugin implements Plugin<Project> {
    @Override
    void apply(Project project) {

        println "=========================="
        println "Javassist开始修改class"
        println "=========================="

        project.logger.debug("===============PhenixPlugin================")


        Task phenixTask = project.tasks.create("phenix", TestTask)
        phenixTask.message = '你好'
        phenixTask.recipient = '中国'
        phenixTask.group = 'plugin-phenix'
        phenixTask.description = '自定义插件task'
    }
}
