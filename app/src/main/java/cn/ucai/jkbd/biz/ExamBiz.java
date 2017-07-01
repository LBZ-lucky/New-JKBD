package cn.ucai.jkbd.biz;

import cn.ucai.jkbd.dao.ExamDao;
import cn.ucai.jkbd.dao.IExamDao;

/**
 * Created by Zzz on 2017/7/1.
 */

public class ExamBiz implements IExamBiz {
    IExamDao dao;
    public ExamBiz()
    {
        dao=new ExamDao();
    }
    @Override
    public void beginExam() {
        examDao.LoadExamInfo();
        examDao.LoadQuestionLists();

    }

    @Override
    public void nextQuestion() {

    }

    @Override
    public void preQuestion() {

    }

    @Override
    public void commitExam() {

    }
}
