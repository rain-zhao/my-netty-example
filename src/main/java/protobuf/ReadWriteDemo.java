package protobuf;

import com.google.protobuf.InvalidProtocolBufferException;
import protobuf.bean.SampleMessageOuterClass;
import protobuf.bean.SearchRequestOuterClass;

/**
 * 测试protobuf的序列化和反序列化
 */
public class ReadWriteDemo {

    public static void main(String[] args) throws InvalidProtocolBufferException {
//        test1();
        test2();

    }

    public static void test1() throws InvalidProtocolBufferException {

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

    public static void test2(){
        SampleMessageOuterClass.SampleMessage.Builder builder = SampleMessageOuterClass.SampleMessage.newBuilder();

        System.out.println("testoneofcase:"+builder.getTestOneofCase().toString());

        builder.setName("rain.zhao");

        System.out.println("name:"+builder.getName());

        System.out.println("testoneofcase:"+builder.getTestOneofCase().toString());

        builder.setSubMessage("this is one message");

        System.out.println("name:"+builder.getName());

        System.out.println("testoneofcase:"+builder.getTestOneofCase().toString());

        System.out.println(builder.build());
    }



}
