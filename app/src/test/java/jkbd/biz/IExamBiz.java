package jkbd.biz;

import cn.ucai.jkbd.bean.Exam;

/**
 * Created by Zzz on 2017/7/1.
 */

public interface IExamBiz {
    void beginExam();
    Exam nextQuestion();
    Exam preQuestion();
    int commitExam();
    Exam getExam();
    Exam getExam(int index);
    String getExamIndex();

}
