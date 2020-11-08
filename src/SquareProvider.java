public class SquareProvider implements DataProvider<Integer, String> {

    @Override
    public String get(Integer key) {
        return (key*key)+"";
    }
}
