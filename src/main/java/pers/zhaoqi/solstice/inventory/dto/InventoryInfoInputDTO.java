package pers.zhaoqi.solstice.inventory.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @description:
 * @author: starr
 * @Date: 2019/5/12 11:59
 **/
@Data
@Accessors(chain = true)
public class InventoryInfoInputDTO {

    @ApiModelProperty(value = "产品ID")
    private Integer productId;

    @ApiModelProperty(value = "库存数量")
    private Integer number;

    private String barCode;

    private Integer brandId;

    private Integer categorySn;

    private String name;

    private String specification;

    private List<LocalDateTime> creatTimeRange;//传入一个size=2的list，开始与结束的时间
}
