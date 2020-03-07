import com.google.protobuf.Any;
import com.google.protobuf.InvalidProtocolBufferException;
import com.google.protobuf.util.JsonFormat;
import org.junit.Test;
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


}
