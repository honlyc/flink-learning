/*
 * Copyright (c) 2010-2016 EEFUNG Software Co.Ltd. All rights reserved.
 * 版权所有(c)2010-2016湖南蚁坊软件有限公司。保留所有权利。
 */

package com.honlyc.flink;

import org.apache.flink.api.common.functions.FlatMapFunction;
import org.apache.flink.api.java.DataSet;
import org.apache.flink.api.java.ExecutionEnvironment;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.util.Collector;

/**
 * 创建日期 2020/09/18
 *
 * @since 1.0.0
 */
public class WordCountJob {
    public static void main(String[] args) throws Exception {
        // 创建 flink 运行的上下文环境
        ExecutionEnvironment env = ExecutionEnvironment.getExecutionEnvironment();
        // 创建DataSet，这里我们的输入是一行一行的文本
        DataSet<String> text = env.fromElements(
                "Flink Spark Storm",
                "Flink Flink Flink",
                "Spark Spark Spark",
                "Storm Storm Storm"
        );
        // 通过Flink内置的转换函数进行计算
        DataSet<Tuple2<String, Integer>> counts =
                text.flatMap(new LineSplitter())
                        .groupBy(0)
                        .sum(1);
        //结果打印
        counts.printToErr();
    }

    private static class LineSplitter implements FlatMapFunction<String, Tuple2<String, Integer>> {
        @Override
        public void flatMap(String value, Collector<Tuple2<String, Integer>> out) throws Exception {
            // 将文本分割
            String[] tokens = value.toLowerCase().split("\\W+");

            for (String token : tokens) {
                if (token.length() > 0) {
                    out.collect(new Tuple2<String, Integer>(token, 1));
                }
            }
        }
    }
}
