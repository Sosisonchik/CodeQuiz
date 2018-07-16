package com.example.apple.codequiz.Tools.Pojo;

public class Quiz {
    private String answA;
    private String answB;
    private String answC;
    private String answD;

    private String question;
    private String rightAnswer;
    private String imgUrl;

    public Quiz() {}

    public Quiz(String answA, String answB, String answC, String answD, String question, String rightAnswer, String imgUrl) {
        this.answA = answA;
        this.answB = answB;
        this.answC = answC;
        this.answD = answD;
        this.question = question;
        this.rightAnswer = rightAnswer;
        this.imgUrl = imgUrl;
    }

    public String getAnswA() {
        return answA;
    }

    public void setAnswA(String answA) {
        this.answA = answA;
    }

    public String getAnswB() {
        return answB;
    }

    public void setAnswB(String answB) {
        this.answB = answB;
    }

    public String getAnswC() {
        return answC;
    }

    public void setAnswC(String answC) {
        this.answC = answC;
    }

    public String getAnswD() {
        return answD;
    }

    public void setAnswD(String answD) {
        this.answD = answD;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getRightAnswer() {
        return rightAnswer;
    }

    public void setRightAnswer(String rightAnswer) {
        this.rightAnswer = rightAnswer;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();

        result.append("\nQuestion: " + this.question);
        result.append("Right answer: " + this.rightAnswer + "\n");
        result.append("ImgUrl: " + this.imgUrl + "\n");
        result.append("\tanswA: " + this.answA + "\n");
        result.append("\tanswB: " + this.answB + "\n");
        result.append("\tanswC: " + this.answC + "\n");
        result.append("\tanswD: " + this.answD + "\n");

        return result.toString();
    }
}
