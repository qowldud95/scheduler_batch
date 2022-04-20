package test;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

@Slf4j
public class test {

    //숫자 list param 5개 받아와서 그중에 제일 큰숫자 가져오기
    //1,2,5,5,6
    private List test(int a, int b, int c, int d, int e){
        Integer[] list = {a,b,c,d,e};
        List result = new ArrayList();

        int max = 0;

        for(int i = 0; i < list.length; i++){
            if(max < list[i]) max = list[i];
        }
        for(int i=0; i < list.length; i++){
            if(list[i] == max){
                result.add(i);
            }
        }
        return result;
    }
}
