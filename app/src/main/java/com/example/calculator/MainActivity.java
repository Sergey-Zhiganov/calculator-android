package com.example.calculator;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    private TextView display;
    private String text = "";
    private String current_value = "";
    private boolean is_current_double = false;
    private boolean is_dot = false;
    private boolean is_sqrt = false;
    private boolean is_sqr = false;
    private String first_value = "";
    private boolean is_first_double = false;
    private String operator = "";
    private  boolean is_need_operator = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        display = findViewById(R.id.display);
    }

    public void onButtonClick(View view) {
        Button button = (Button) view;
        String buttonText = button.getText().toString();
        switch (buttonText) {
            case "C":
                clearDisplay();
                return;
            case "/":
            case "*":
            case "-":
            case "+":
            case "%":
            case "x²":
                setOperator(buttonText);
                break;
            case "√":
                setSqrt(buttonText);
                break;
            case ".":
                setDot(buttonText);
                break;
            case "=":
                calculate();
                return;
            default:
                if (Objects.equals(text, "") && Objects.equals(buttonText, "0") || is_need_operator)
                    return;
                is_dot = false;
                current_value += buttonText;
                text += buttonText;
        }
        if (!Objects.equals(text, ""))
            display.setText(text);
    }
    private void clearDisplay() {
        display.setText("0");
        text = "";
        operator = "";
        current_value = "";
        is_current_double = false;
        first_value = "";
        is_first_double = false;
        is_sqrt = false;
        is_dot = false;
        is_sqr = false;
        is_need_operator = false;
    }
    private void setOperator(String op) {
        if (!Objects.equals(current_value, "") && !is_dot) {
            if (Objects.equals(op, "%")) {
                if (is_need_operator)
                    return;
                if (Double.parseDouble(current_value) % 100 != 0)
                    is_current_double = true;
                if (is_current_double) {
                    current_value = String.valueOf(Double.parseDouble(current_value) / 100);
                }
                else {
                    current_value = String.valueOf(Integer.parseInt(current_value) / 100);
                }
                is_need_operator = true;
                text += op;
            }
            else if (Objects.equals(op, "x²")) {
                if (is_need_operator)
                    return;
                if (!is_sqr) {
                    if (is_current_double)
                        current_value = String.valueOf(Math.pow(Double.parseDouble(current_value), 2));
                    else
                        current_value = String.valueOf(Math.pow(Integer.parseInt(current_value), 2));
                }
                else
                    current_value = current_value.substring(1);
                is_need_operator = true;
                text += "²";
                is_sqr = true;
            }
            else {
                if (is_sqrt) {
                    if (is_current_double)
                        current_value = String.valueOf(Double.parseDouble(current_value));
                    else
                        current_value = String.valueOf(Integer.parseInt(current_value));
                    is_sqrt = false;
                }
                if (!Objects.equals(operator, ""))
                    calculate();
                operator = op;
                first_value = current_value;
                is_first_double = is_current_double;
                current_value = "";
                is_current_double = false;
                is_sqr = false;
                text += op;
            }

            if (!Objects.equals(op, "%") && !Objects.equals(op, "x²"))
                is_need_operator = false;
        }
        else if (Objects.equals(current_value, "") && Objects.equals(op, "-") && Objects.equals(first_value, "")) {
            text += op;
            current_value += op;
            is_dot = true;
        }
    }
    private void setSqrt(String op) {
        if (Objects.equals(current_value, "") && !is_sqrt) {
            text += op;
            is_sqrt = true;
        }
    }
    private void setDot(String op) {
        if (!is_dot && !Objects.equals(current_value, "")) {
            text += op;
            current_value += op;
            is_dot = true;
            is_current_double = true;
        }
    }
    private void calculate() {
        if (is_sqrt && !Objects.equals(current_value, "") && !is_dot) {
            try {
                if (is_current_double)
                    current_value = String.valueOf(Math.sqrt(Double.parseDouble(current_value)));
                else
                    current_value = String.valueOf(Math.sqrt(Integer.parseInt(current_value)));
            }
            catch (Exception e) {
                display.setText("Ошибка");
                return;
            }
            if (Objects.equals(first_value, "")) {
                text = current_value;
                display.setText(text);
            }
        }
        if (!Objects.equals(operator, "") && !Objects.equals(first_value, "") && !Objects.equals(current_value, "") && !is_dot) {
            switch (operator) {
                case "/":
                    if (!Objects.equals(current_value, "0")) {
                        if (Double.parseDouble(first_value) % Double.parseDouble(current_value) != 0)
                            is_first_double = true;
                        if (is_first_double || is_current_double)
                            text = String.valueOf(Double.parseDouble(first_value) / Double.parseDouble(current_value));
                        else
                            text = String.valueOf(Integer.parseInt(first_value) / Integer.parseInt(current_value));
                    }
                    else {
                        display.setText("Ошибка");
                        return;
                    }
                    break;
                case "*":
                    if (is_first_double || is_current_double)
                        text = String.valueOf(Double.parseDouble(first_value) * Double.parseDouble(current_value));
                    else
                        text = String.valueOf(Integer.parseInt(first_value) * Integer.parseInt(current_value));
                    break;
                case "-":
                    if (is_first_double || is_current_double)
                        text = String.valueOf(Double.parseDouble(first_value) - Double.parseDouble(current_value));
                    else
                        text = String.valueOf(Integer.parseInt(first_value) - Integer.parseInt(current_value));
                    break;
                case "+":
                    if (is_first_double || is_current_double)
                        text = String.valueOf(Double.parseDouble(first_value) + Double.parseDouble(current_value));
                    else
                        text = String.valueOf(Integer.parseInt(first_value) + Integer.parseInt(current_value));
                    break;
            }
            
            if (is_first_double || is_current_double)
                is_current_double = true;
            else
                is_current_double = false;
            first_value = "";
            is_first_double = false;
            current_value = text;
            operator = "";
            is_sqr = false;
            is_sqrt = false;
            display.setText(text);
        }
        else if (is_sqr || is_need_operator) {
            is_sqr = false;
            is_need_operator = false;
            text = current_value;
            display.setText(text);
        }
    }
}
