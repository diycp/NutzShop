package cn.wizzer.app.web.modules.controllers.platform.goods;

import cn.wizzer.app.goods.modules.models.Goods_brand;
import cn.wizzer.app.goods.modules.services.GoodsBrandService;
import cn.wizzer.app.web.commons.slog.annotation.SLog;
import cn.wizzer.framework.base.Result;
import cn.wizzer.framework.page.datatable.DataTableColumn;
import cn.wizzer.framework.page.datatable.DataTableOrder;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.nutz.dao.Cnd;
import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.log.Log;
import org.nutz.log.Logs;
import org.nutz.mvc.adaptor.WhaleAdaptor;
import org.nutz.mvc.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@IocBean
@At("/platform/goods/brand")
public class GoodsBrandController {
	private static final Log log = Logs.get();
	@Inject
	private GoodsBrandService shopGoodsBrandService;

	@At("")
	@Ok("beetl:/platform/goods/brand/index.html")
	@RequiresAuthentication
	public void index() {

	}

	@At
	@Ok("json:full")
	@RequiresAuthentication
	public Object data(@Param("length") int length, @Param("start") int start, @Param("draw") int draw, @Param("::order") List<DataTableOrder> order, @Param("::columns") List<DataTableColumn> columns) {
		Cnd cnd = Cnd.NEW();
    	return shopGoodsBrandService.data(length, start, draw, order, columns, cnd, null);
    }

    @At
    @Ok("beetl:/platform/goods/brand/add.html")
    @RequiresAuthentication
    public void add() {

    }

    @At
    @Ok("json")
    @SLog(tag = "新建品牌", msg = "新建品牌")
	@RequiresPermissions("goods.conf.brand.add")
	@AdaptBy(type = WhaleAdaptor.class)
    public Object addDo(@Param("..") Goods_brand shopGoodsBrand, HttpServletRequest req) {
		try {
			shopGoodsBrandService.insert(shopGoodsBrand);
			return Result.success("system.success");
		} catch (Exception e) {
			return Result.error("system.error");
		}
    }

    @At("/edit/?")
    @Ok("beetl:/platform/goods/brand/edit.html")
    @RequiresAuthentication
    public Object edit(String id) {
		return shopGoodsBrandService.fetch(id);
    }

    @At
    @Ok("json")
    @SLog(tag = "修改品牌", msg = "ID:${args[0].id}")
	@RequiresPermissions("goods.conf.brand.edit")
	@AdaptBy(type = WhaleAdaptor.class)
	public Object editDo(@Param("..") Goods_brand shopGoodsBrand, HttpServletRequest req) {
		try {

			shopGoodsBrand.setOpAt((int) (System.currentTimeMillis() / 1000));
			shopGoodsBrandService.updateIgnoreNull(shopGoodsBrand);
			return Result.success("system.success");
		} catch (Exception e) {
			return Result.error("system.error");
		}
    }


    @At({"/delete","/delete/?"})
    @Ok("json")
    @SLog(tag = "删除品牌", msg = "ID:${args[2].getAttribute('id')}")
	@RequiresPermissions("goods.conf.brand.delete")
	public Object delete(String id, @Param("ids") String[] ids , HttpServletRequest req) {
		try {
			if(ids!=null&&ids.length>0){
				shopGoodsBrandService.delete(ids);
    			req.setAttribute("id", org.apache.shiro.util.StringUtils.toString(ids));
			}else{
				shopGoodsBrandService.delete(id);
    			req.setAttribute("id", id);
			}
			return Result.success("system.success");
		} catch (Exception e) {
			return Result.error("system.error");
		}
    }

}
