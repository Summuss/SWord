package top.summus.sword.entity;

import lombok.*;

import java.io.Serializable;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BookNode implements Serializable {
    private int id;
    @Builder.Default
    private int nodeNo = -1;
    private String nodeName;
    private String nodePath;

    /**
     * 0为folder,1为page
     */
    private int nodeTag;
    private Date nodeChangedDate;
    @Builder.Default
    private int syncStatus = 1;

}

