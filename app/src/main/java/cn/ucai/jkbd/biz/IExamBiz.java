package cn.ucai.jkbd.biz;

/**
 * Created by Zzz on 2017/7/1.
 */

public interface IExamBiz {
    void beginExam();
    void nextQuestion();
    void preQuestion();
    void commitExam();
}
