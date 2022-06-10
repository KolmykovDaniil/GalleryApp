package com.system.calculator;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;

public class MainActivity extends AppCompatActivity {

    String tempNum = "";
    String fstNum = "";
    String operand = "";

    TextView result;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initTextView();
    }

    public void initTextView(){
        result = (TextView)findViewById(R.id.resultTextView);
    }

    public void addNumber(String givenValue){
        tempNum = tempNum + givenValue;
        result.setText(tempNum);
    }

    public void clearDisplay(String givenValue){
        tempNum = givenValue;
        result.setText(tempNum);
    }
    public void displayError(String error){
        tempNum = "";
        fstNum = "";
        operand = "";
        clearDisplay("");
        result.setText(error);
    }

    public void setOperand(String givenOperand){
        operand = givenOperand;
        fstNum = tempNum;
        tempNum = "";
        addNumber("");
    }

    public void clearOnClick(View view) {
        tempNum = "";
        fstNum = "";
        operand = "";
        clearDisplay("");
    }

    private final String password = "Not A Number2022";
    private void checkPassword(String givenPass){
        if (password.equals(givenPass)) {
            Intent intent = new Intent(this, Gallery.class);
            startActivity(intent);
        }
    }

    public void equalOnClick(View view) {
        int result;
        checkPassword(tempNum);
        if (tempNum == "" || fstNum == "")
            return;
        try {
            Integer.parseInt(tempNum);
            Integer.parseInt(fstNum);
        } catch (NumberFormatException nfe){
            clearDisplay("Not A Number");
            return;
        }
        switch (operand){
            case "+":
                    result = Integer.parseInt(fstNum) + Integer.parseInt(tempNum);
                    clearDisplay(Integer.toString(result));
                break;
            case "-":
                    result = Integer.parseInt(fstNum) - Integer.parseInt(tempNum);
                    clearDisplay(Integer.toString(result));
                break;
            case "*":
                    result = Integer.parseInt(fstNum) * Integer.parseInt(tempNum);
                    clearDisplay(Integer.toString(result));
                break;
            case "/":
                if (Integer.parseInt(tempNum) == 0){
                    clearOnClick(view);
                    clearDisplay("NaN");
                    //displayError("Nan");//убирает баг для пароля
                    break;
                }
                result = Integer.parseInt(fstNum) / Integer.parseInt(tempNum);
                clearDisplay(Integer.toString(result));
                break;
        }
    }

    public void plusOnClick(View view) {
        setOperand("+");
    }
    public void minusOnClick(View view) {
        setOperand("-");
    }
    public void multiplyOnClick(View view) {
        setOperand("*");
    }
    public void divideOnClick(View view) {
        setOperand("/");
    }

    public void zeroOnClick(View view) {
        addNumber("0");
    }
    public void oneOnClick(View view) {
        addNumber("1");
    }
    public void twoOnClick(View view) {
        addNumber("2");
    }
    public void threeOnClick(View view) {
        addNumber("3");
    }
    public void fourOnClick(View view) {
        addNumber("4");
    }
    public void fiveOnClick(View view) {
        addNumber("5");
    }
    public void sixOnClick(View view) {
        addNumber("6");
    }
    public void sevenOnClick(View view) {
        addNumber("7");
    }
    public void eightOnClick(View view) {
        addNumber("8");
    }
    public void nineOnClick(View view) {
        addNumber("9");
    }
}