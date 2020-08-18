package com.zhuinden.sparkexperiment;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Count implements Serializable {
    private String word;
    private double count;

//    public Count() {
//    }
//
//    public Count(String word, long count) {
//        this.word = word;
//        this.count = count;
//    }
//
//    public String getWord() {
//        return word;
//    }
//
//    public void setWord(String word) {
//        this.word = word;
//    }
//
//    public long getCount() {
//        return count;
//    }
//
//    public void setCount(long count) {
//        this.count = count;
//    }
}
