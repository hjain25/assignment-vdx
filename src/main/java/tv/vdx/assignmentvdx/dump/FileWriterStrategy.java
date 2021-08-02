package tv.vdx.assignmentvdx.dump;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

/**
 * file writer strategy i.e. writes to file
 */
@Slf4j
public class FileWriterStrategy implements DataWriteStrategy {

    @Override
    public void write(Object data) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("src/main/resources/dump/cacheData.txt", true))) {
            writer.newLine();
            writer.append(data.toString());
        } catch (IOException e) {
            log.error("data write op data to file failed : ", e);
        }
    }

    @Override
    public DataWriteStrategyEnum getStrategyName() {
        return DataWriteStrategyEnum.FILE_WRITER;
    }
}
