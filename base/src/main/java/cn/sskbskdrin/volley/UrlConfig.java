package cn.sskbskdrin.volley;

/**
 * Created by ayke on 2016/8/29 0029.
 */
public class UrlConfig {
	private static final String BASE_URL = "http://weixin.ccswdata.com/order/";

	public static final String LOGIN_URL = BASE_URL + "index.php?m=Admin&c=App&a=login";
	public static final String LOGOUT_URL = BASE_URL + "index.php?m=Admin&c=App&a=logout";
	public static final String PERSON_INFO_URL = BASE_URL + "index.php?m=Admin&c=App&a=personInfo";
	public static final String ORDER_LIST_URL = BASE_URL + "index.php?m=Admin&c=App&a=myorder";
	public static final String ORDER_DETAIL_URL = BASE_URL + "index.php?m=Admin&c=App&a=orderInfo";
	public static final String GET_ORDER_INFO_URL = BASE_URL + "index.php?m=Admin&c=App&a=getOrder";
	public static final String GRAB_ORDER_INFO_URL = BASE_URL + "index.php?m=Admin&c=App&a=confirmOrder";
	public static final String REFUSE_ORDER_URL = BASE_URL + "index.php?m=Admin&c=App&a=cancelOrder";
	public static final String ORDER_MESSAGE_URL = BASE_URL + "index.php?m=Admin&c=App&a=onserve";
	public static final String ADD_ORDER_REMARK_URL = BASE_URL + "index.php?m=Admin&c=App&a=addRemark";

}
