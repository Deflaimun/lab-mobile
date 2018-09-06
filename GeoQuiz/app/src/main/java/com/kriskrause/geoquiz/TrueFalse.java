package com.kriskrause.geoquiz;

public class TrueFalse {
    public int getQuestion() {
        return _question;
    }

    public void setQuestion(int question) {
        this._question = question;
    }

    public boolean isTrueQuestion() {
        return _trueQuestion;
    }

    public void setTrueQuestion(boolean trueQuestion) {
        this._trueQuestion = trueQuestion;
    }

    public boolean hasCheated() { return _hasCheated; };

    public void setHasCheated(boolean hasCheated) { this._hasCheated = hasCheated; };

    private int _question;
    private boolean _trueQuestion;
    private boolean _hasCheated;

    public TrueFalse(int question, boolean trueQuestion) {
        _question = question;
        _trueQuestion = trueQuestion;
        _hasCheated = false;
    }
}
