package net.ddns.spellbank.day07;

public class Hand implements Comparable<Hand> {

    private final String cards;
    private final int[] values;
    private int score;
    private final long bid;
    private final boolean jacksWild;

    public int getScore() {
        return score;
    }

    public Hand(String cards, long bid, boolean jacksWild) {
        if (cards.length() != 5) throw new IllegalArgumentException("Invalid hand: " + cards);
        this.cards = cards;
        this.bid = bid;
        this.jacksWild = jacksWild;
        int jackValue = jacksWild ? 1 : 11;
        values = new int[cards.length()];
        for (int i = 0; i < cards.length(); i++) {
            var c = cards.charAt(i);
            if (Character.isDigit(c)) values[i] = c - '0';
            else {
                values[i] = switch (c) {
                    case 'A':
                        yield 14;
                    case 'K':
                        yield 13;
                    case 'Q':
                        yield 12;
                    case 'J':
                        yield jackValue;
                    case 'T':
                        yield 10;
                    default:
                        throw new IllegalArgumentException("Invalid hand: " + cards);
                };
            }
        }
        setScore();
    }

    public long getBid() {
        return bid;
    }

    @Override
    public String toString() {
        return cards;
    }

    private int getCardValue(int n) {
        return values[n];
    }

    private void setScore() {
        var counts = new int[15];
        for (var v : values) counts[v]++;
        int first = 0;
        int second = 0;
        for (int i = 2; i < counts.length; i++) {
            int c = counts[i];
            if (c > first) {
                second = first;
                first = c;
            } else if (c > second) second = c;
        }
        if (jacksWild) first += counts[1];
        if (first == 5) score = 6;
        else if (first == 4) score = 5;
        else if (first == 3) score = second == 2 ? 4 : 3;
        else if (first == 2) score = second == 2 ? 2 : 1;
        else score = 0;
    }

    @Override
    public int compareTo(Hand o) {
        if (getScore() == o.getScore()) {
            for (int i = 0; i < values.length; i++) {
                if (values[i] != o.getCardValue(i)) return values[i] - o.getCardValue(i);
            }
            return 0;
        }
        return getScore() - o.getScore();
    }
}
