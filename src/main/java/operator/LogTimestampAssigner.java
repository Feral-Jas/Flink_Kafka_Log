package operator;

import org.apache.flink.api.common.eventtime.*;

/**
 * @author liuchenyu
 * @date 2020/11/6
 */
public class LogTimestampAssigner implements WatermarkStrategy<String> {

    @Override
    public TimestampAssigner<String> createTimestampAssigner(TimestampAssignerSupplier.Context context) {
        //context.getMetricGroup().
        return null;
    }

    @Override
    public WatermarkGenerator<String> createWatermarkGenerator(WatermarkGeneratorSupplier.Context context) {
        return null;
    }

}
