package com.bzg.cloud.controller;

import com.bzg.cloud.service.SolrService;
import com.bzg.cloud.vo.BaseResponse;
import com.bzg.cloud.vo.CreateRequest;
import com.bzg.cloud.vo.QueryRequest;
import com.bzg.cloud.vo.SystemStatusResponse;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpSession;
import java.lang.management.ManagementFactory;
import java.lang.management.MemoryMXBean;
import java.lang.management.MemoryUsage;

/**
 * @author: syl  Date: 2018/5/11 Email:nerosyl@live.com
 */
@RestController
@RequestMapping("/v1/solrAdmin")
public class SolrController extends BaseController {
    private static final Logger log = LoggerFactory.getLogger(SolrController.class);
    @Autowired
    SolrService solrService;

    @ApiOperation(value = "导入增量表数据", notes = "")
    @ApiImplicitParams({@ApiImplicitParam(paramType = "form", dataType = "int", name = "startPosition"),
            @ApiImplicitParam(paramType = "form", dataType = "int", name = "maxResult")})
    @RequestMapping(value = "deltaImport", method = RequestMethod.POST)
    @ResponseBody
    public BaseResponse deltaImport(@ApiIgnore Integer startPosition, @ApiIgnore Integer maxResult, HttpSession httpSession) {
        try {
            httpSession.setAttribute("IMPORT_STATUS", "RUNNING");
            solrService.deltaImport(startPosition, maxResult);
            httpSession.setAttribute("IMPORT_STATUS", "FINISHED");
            return buildSuccessResp("deltaImport import success");
        } catch (Exception e) {
            log.error("import error", e);
            return buildErrorResp("error" + e.getMessage());
        }
    }

    @ApiOperation(value = "全量数据导入", notes = "")
    @ApiImplicitParams({@ApiImplicitParam(paramType = "form", dataType = "int", name = "startPosition"),
            @ApiImplicitParam(paramType = "form", dataType = "int", name = "maxResult")})
    @RequestMapping(value = "fullImport", method = RequestMethod.POST)
    @ResponseBody
    public BaseResponse fullImport(@ApiIgnore Integer startPosition, @ApiIgnore Integer maxResult, HttpSession httpSession) {
        try {
            httpSession.setAttribute("IMPORT_STATUS", "RUNNING");
            solrService.fullImport(startPosition, maxResult);
            httpSession.setAttribute("IMPORT_STATUS", "FINISHED");
            return buildSuccessResp("deltaImport import success");
        } catch (Exception e) {
            log.error("full import error", e);
            return buildErrorResp("error" + e.getMessage());
        }
    }

    @ApiOperation(value = "查询导入状态", notes = "")
    @RequestMapping(value = "status", method = RequestMethod.GET)
    @ResponseBody
    public BaseResponse systemStatus(HttpSession httpSession) {
        SystemStatusResponse response = new SystemStatusResponse();
        MemoryMXBean memoryMBean = ManagementFactory.getMemoryMXBean();
        MemoryUsage usage = memoryMBean.getHeapMemoryUsage();
        Object sessionAttribute = httpSession.getAttribute("IMPORT_STATUS");
        if (sessionAttribute != null) {
            response.setMessage(sessionAttribute.toString());
        }
        response.setInit((usage.getInit() / 1024 / 1024) + "mb");
        response.setMax((usage.getMax() / 1024 / 1024) + "mb");
        response.setRam((usage.getUsed() / 1024 / 1024) + "mb");

        return buildSuccessResp(response);

    }


    @ApiOperation(value = "查询", notes = "")
    @ApiImplicitParams({@ApiImplicitParam(paramType = "query", dataType = "string", name = "qValue")
            ,@ApiImplicitParam(paramType = "query", dataType = "string", name = "fqValue")
            ,@ApiImplicitParam(paramType = "query", dataType = "string", name = "sortValue")
            ,@ApiImplicitParam(paramType = "query", dataType = "string", name = "dfValue")
            ,@ApiImplicitParam(paramType = "query", dataType = "string", name = "sortOrder")
            ,@ApiImplicitParam(paramType = "query", dataType = "string", name = "sortField")
            ,@ApiImplicitParam(paramType = "query", dataType = "string", name = "highlightField")
            ,@ApiImplicitParam(paramType = "query", dataType = "string", name = "simplePre")
            ,@ApiImplicitParam(paramType = "query", dataType = "string", name = "simplePost")
            ,@ApiImplicitParam(paramType = "query", dataType = "int", name = "start")
            ,@ApiImplicitParam(paramType = "query", dataType = "int", name = "rows")
            ,@ApiImplicitParam(paramType = "query", dataType = "string", name = "collectionName")
    })
    @RequestMapping(value = "query", method = RequestMethod.GET)
    @ResponseBody
    public BaseResponse query( @ApiIgnore QueryRequest request) {
        try {
            return buildSuccessResp(" success",  solrService.query(request));
        } catch (Exception e) {
            log.error(" error", e);
            return buildErrorResp("delete error" + e.getMessage());
        }
    }

    @ApiOperation(value = "创建集群", notes = "")
    @ApiImplicitParams({@ApiImplicitParam(paramType = "form", dataType = "string", name = "collection")
            ,@ApiImplicitParam(paramType = "form", dataType = "string", name = "config")
            ,@ApiImplicitParam(paramType = "form", dataType = "int", name = "numShards")
            ,@ApiImplicitParam(paramType = "form", dataType = "int", name = "numReplicas")
    })
    @RequestMapping(value = "create", method = RequestMethod.POST)
    @ResponseBody
    public BaseResponse create( @ApiIgnore CreateRequest request) {
        try {
            return buildSuccessResp(" success",  solrService.createCollection(request));
        } catch (Exception e) {
            log.error(" error", e);
            return buildErrorResp("create collection error" + e.getMessage());
        }
    }

        @ApiOperation(value = "ClearAll", notes = "")
        @RequestMapping(value = "clearAll", method = RequestMethod.POST)
        @ResponseBody
        public BaseResponse clearAll () {
            try {
                solrService.clearAllData();
                return buildSuccessResp("delete all data success");
            } catch (Exception e) {
                log.error("delete error", e);
                return buildErrorResp("delete error" + e.getMessage());
            }


        }


    }
