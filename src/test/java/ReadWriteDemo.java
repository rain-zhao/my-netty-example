import com.google.protobuf.Any;
import com.google.protobuf.InvalidProtocolBufferException;
import com.google.protobuf.util.JsonFormat;
import org.junit.Test;
import protoBuf.bean.UserSearchResult2OuterClass;
import protoBuf.bean.UserSearchResultOuterClass;
import protobuf.bean.PageResultOuterClass;
import protobuf.bean.SampleMessageOuterClass;
import protobuf.bean.SearchRequestOuterClass;
import protobuf.bean.UserOuterClass;

/**
 * 测试protobuf的序列化和反序列化
 */
public class ReadWriteDemo {

    @Test
    public void test() throws InvalidProtocolBufferException {

        //类构造器
        SearchRequestOuterClass.SearchRequest.Builder builder = SearchRequestOuterClass.SearchRequest.newBuilder();
        builder.setQuery("select * from t_user").setPageNumber(1).setResultPerPage(3);

        //创建对象
        SearchRequestOuterClass.SearchRequest req = builder.build();

        //序列化
        byte[] bytes = req.toByteArray();

        //反序列化
        SearchRequestOuterClass.SearchRequest newReq = SearchRequestOuterClass.SearchRequest.parseFrom(bytes);

        System.out.println(req.equals(newReq));

    }

    @Test
    public void testOneofFeature() {
        SampleMessageOuterClass.SampleMessage.Builder builder = SampleMessageOuterClass.SampleMessage.newBuilder();

        System.out.println("testoneofcase:" + builder.getTestOneofCase().toString());

        builder.setName("rain.zhao");

        System.out.println("name:" + builder.getName());

        System.out.println("testoneofcase:" + builder.getTestOneofCase().toString());

        builder.setSubMessage("this is one message");

        System.out.println("name:" + builder.getName());

        System.out.println("testoneofcase:" + builder.getTestOneofCase().toString());

        System.out.println(builder.build());
    }

    @Test
    public void testAnyFeature() throws InvalidProtocolBufferException {

        //create one user
        UserOuterClass.User.Builder userBuilder = UserOuterClass.User.newBuilder();
        userBuilder.setSex("male").setName("rain.zhao").setId(1);
        UserOuterClass.User user = userBuilder.build();

        //create one page-result
        PageResultOuterClass.PageResult.Builder pageResultBuilder = PageResultOuterClass.PageResult.newBuilder();
        pageResultBuilder.setMsg("success").setStatus(1);
        pageResultBuilder.setData(Any.pack(user));
        PageResultOuterClass.PageResult pageResult = pageResultBuilder.build();

        //print toString
        System.out.println("pageResult:" + pageResult);

        JsonFormat.TypeRegistry typeRegistry = JsonFormat.TypeRegistry.newBuilder().add(UserOuterClass.User.getDescriptor()).build();

        //print json
        System.out.println("pageResult json:" + JsonFormat.printer().usingTypeRegistry(typeRegistry).print(pageResult));

        //序列化
        byte[] bytes = pageResult.toByteArray();

        //反序列化
        PageResultOuterClass.PageResult parsePageResult = PageResultOuterClass.PageResult.parseFrom(bytes);

        //获取user
        Any data = parsePageResult.getData();

        UserOuterClass.User unpackUser = data.unpack(UserOuterClass.User.class);

        System.out.println(unpackUser);
        System.out.println(JsonFormat.printer().print(unpackUser));


    }

    @Test
    public void testMapFeature() throws InvalidProtocolBufferException {

        UserOuterClass.User.Builder ub = UserOuterClass.User.newBuilder();
        ub.setSex("male").setName("rain.zhao").setId(1);
        UserOuterClass.User u1 = ub.build();
        ub.setSex("female").setName("sh.chen").setId(2);
        UserOuterClass.User u2 = ub.build();

        //result1
        protoBuf.bean.UserSearchResultOuterClass.UserSearchResult.Builder builder = protoBuf.bean.UserSearchResultOuterClass.UserSearchResult.newBuilder();
        builder.setSize(2).putData("rain.zhao",u1).putData("sh.chen",u2);
        protoBuf.bean.UserSearchResultOuterClass.UserSearchResult result = builder.build();

        //result2
        protoBuf.bean.UserSearchResult2OuterClass.UserSearchResult2.Builder builder2 = protoBuf.bean.UserSearchResult2OuterClass.UserSearchResult2.newBuilder();
        UserSearchResult2OuterClass.UserSearchResult2.entry.Builder entryBuilder = UserSearchResult2OuterClass.UserSearchResult2.entry.newBuilder();
        builder2.setSize(2)
                .addData(entryBuilder.setKey("rain.zhao").setValue(u1).build())
                .addData(entryBuilder.setKey("sh.chen").setValue(u2).build());

        protoBuf.bean.UserSearchResult2OuterClass.UserSearchResult2 result2 = builder2.build();

        //解析成json
        System.out.println(JsonFormat.printer().print(result));
        System.out.println(JsonFormat.printer().print(result2));
//        //序列化
//        byte[] bytes = result.toByteArray();
//
//        //反序列化
//        protoBuf.bean.UserSearchResultOuterClass.UserSearchResult parseResult = protoBuf.bean.UserSearchResultOuterClass.UserSearchResult.parseFrom(bytes);


//        System.out.println(parseResult);


    }


    @Test
    /**
     * 测试不同的json格式化参数
     */
    public void testJsonFormat() throws InvalidProtocolBufferException {
        UserSearchResultOuterClass.UserSearchResult.Builder builder = UserSearchResultOuterClass.UserSearchResult.newBuilder();

        JsonFormat.Printer printer = JsonFormat.printer();

        //默认参数下解析成json
        System.out.println(printer.print(builder));

        //设置打印所有default value,默认default value不打印出来
        printer = printer.includingDefaultValueFields();
        System.out.println(printer.print(builder));


    }


}
