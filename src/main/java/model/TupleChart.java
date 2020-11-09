package model;

import org.apache.flink.api.java.tuple.Tuple6;

/**
 * @author liuchenyu
 * @date 2020/11/5
 */
public class TupleChart extends Tuple6<Long,Long,Long,Long,Long,Long> {
    public TupleChart(){}
    public TupleChart(Long f0,Long f1,Long f2,Long f3,Long f4,Long f5){
        this.f0=f0;
        this.f1=f1;
    }
    //public Long dataSizeIn
}
