package pers.zhaoqi.solstice.product.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import pers.zhaoqi.solstice.brand.service.IBrandInfoService;
import pers.zhaoqi.solstice.common.enums.ResultCodeAndMessage;
import pers.zhaoqi.solstice.common.result.ActionResult;
import pers.zhaoqi.solstice.common.result.Result;
import pers.zhaoqi.solstice.common.utils.Utils;
import pers.zhaoqi.solstice.dictionary.service.IDataDictionaryService;
import pers.zhaoqi.solstice.product.dto.ProductInfoInputDTO;
import pers.zhaoqi.solstice.product.entity.ProductInfo;
import pers.zhaoqi.solstice.product.service.IProductInfoService;

import java.util.List;

//import pers.zhaoqi.solstice.common.dto.JSONArrayInputDTO;


/**
 * <p>
 * 商品信息表 前端控制器
 * </p>
 *
 * @author zachary.zhao
 * @since 2019-04-14
 */
@RestController
@RequestMapping("/product/productInfo")
public class ProductInfoController {

    public static final String MESSAGE_HEAD = "[产品]";

    Logger logger= LoggerFactory.getLogger(ProductInfo.class);

    @Autowired
    private IProductInfoService productInfoService;
    @Autowired
    private IDataDictionaryService dataDictionaryService;
    @Autowired
    private IBrandInfoService brandInfoService;

    @GetMapping()
    public ActionResult queryProduct(ProductInfoInputDTO productInfoInputDTO) {
        try {
            ProductInfo productInfo = new ProductInfo();
            BeanUtils.copyProperties(productInfoInputDTO, productInfo);
            QueryWrapper queryWrapper = new QueryWrapper(productInfo);
            List list = productInfoService.list(queryWrapper);
            return Result.success(MESSAGE_HEAD + ResultCodeAndMessage.SUCCESS_QUERY_MESSAGE, list);
        }catch (Exception e){
            logger.debug(MESSAGE_HEAD+"queryProduct"+e.getMessage());
            return Result.failed(ResultCodeAndMessage.FAIL_QUERY,MESSAGE_HEAD+ResultCodeAndMessage.FAIL_QUERY_MESSAGE);
        }
    }

    @DeleteMapping()
    @ResponseBody()
    public ActionResult removeProduct(@RequestBody List<Integer> idList){
        try {
            ProductInfo productInfo=new ProductInfo();
            QueryWrapper queryWrapper=new QueryWrapper(productInfo);
            queryWrapper.in("id",idList);
            productInfoService.remove(queryWrapper);

            return Result.success(MESSAGE_HEAD + ResultCodeAndMessage.SUCCESS_REMOVE_MESSAGE);
        }catch (Exception e){
            logger.debug(MESSAGE_HEAD+"removeProduct"+e.getMessage());
            return Result.failed(ResultCodeAndMessage.FAIL_REMOVE,MESSAGE_HEAD+ResultCodeAndMessage.FAIL_REMOVE_MESSAGE);
        }
    }

    @PostMapping("/{name}")
    public ActionResult SaveProduct(@RequestBody ProductInfo productInfo) {
        QueryWrapper queryWrapper = new QueryWrapper(new ProductInfo().setName(productInfo.getName()));
        if (productInfoService.count(queryWrapper) > 0) {
            return Result.failed(ResultCodeAndMessage.FAIL_SAVA, MESSAGE_HEAD + ResultCodeAndMessage.FAIL_INPUT_DATA_REPEAT_MESSAGE + "产品名称");
        }

        Utils.FillCreate(productInfo);
        if (productInfo.getBrandId() != null)
            productInfo.setBrandName(brandInfoService.getById(productInfo.getBrandId()).getName());
        if (productInfo.getCategorySn() != null)
            productInfo.setCategoryName(dataDictionaryService.getById(productInfo.getCategorySn()).getLabelZhCn());
        productInfo.setBarCode(Utils.GeneratesUniqueCode());

        if (productInfoService.save(productInfo)) {
            return Result.success(MESSAGE_HEAD + ResultCodeAndMessage.SUCCESS_SAVA_MESSAGE);
        } else {
            return Result.failed(ResultCodeAndMessage.FAIL_SAVA, MESSAGE_HEAD + ResultCodeAndMessage.FAIL_SAVA_MESSAGE);
        }
    }

}
