package io.github.pandalxb.jlibrehardwaremonitor.model;

import lombok.Data;

import java.util.List;

/**
 * Computer model
 *
 * @author pandalxb
 */
@Data
public class Computer {
    private List<Hardware> hardware;
}
