package com.kovizone.demo.lang;

import java.util.Objects;

/**
 * 区间
 *
 * @author <a href="mailto:kovichen@163.com">KoviChen</a>
 * @version 1.0
 */
public class Interval<N extends Comparable<N>> {

    private Node<N> low;

    private Node<N> high;

    public static <N extends Comparable<N>> Interval<N> createL(N low, N high) {
        return new Interval<>(low, true, high, false);
    }

    public static <N extends Comparable<N>> Interval<N> createH(N low, N high) {
        return new Interval<>(low, false, high, true);
    }

    public static <N extends Comparable<N>> Interval<N> create(N low, N high) {
        return new Interval<>(low, high);
    }

    public static <N extends Comparable<N>> Interval<N> create(N low, Boolean eqLow, N high, Boolean eqHigh) {
        return new Interval<>(low, eqLow, high, eqHigh);
    }

    public Interval(N low, N high) {
        super();
        this.low = new Node<>(low, true);
        this.high = new Node<>(high, true);
    }

    public Interval(N low, Boolean eqLow, N high, Boolean eqHigh) {
        super();
        this.low = new Node<>(low, eqLow);
        this.high = new Node<>(high, eqHigh);
    }

    public Boolean check(N arg) {
        Node<N> n = new Node<>(arg);
        return high.compareTo(n) >= 0 && n.compareTo(low) >= 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(getLow(), getHigh());
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (!(obj instanceof Interval)) {
            return false;
        }
        Interval<N> interval = (Interval) obj;
        boolean result = this.getLow().equals(interval.getLow());
        if (result) {
            result = this.getHigh().equals(interval.getHigh());
        }
        return result;
    }

    /**
     * 对比
     *
     * @param o 被对比对象
     * @return <p>0 - 区间完全相同</p>
     * <p>分离态结果：</p>
     * <p>1 - 本对象的low大于o的high</p>
     * <p>-1 - 本对象的high小于o的low</p>
     * <p>相交态结果：</p>
     * <p>2 - 本对象的high大于o的high，本对象的low大于o的low且小于o的high</p>
     * <p>-2 - 本对象的low小于o的low，本对象的high小于o的high且大于o的low</p>
     * <p>同边态结果：</p>
     * <p>3 - 本对象的high大于o的high，本对象的low等于o的low</p>
     * <p>-3 - 本对象的high小于o的high，本对象的low等于o的low</p>
     * <p>4 - 本对象的low小于o的low，本对象的high等于o的high</p>
     * <p>-4 - 本对象的low大于o的low，本对象的high等于o的high</p>
     * <p>包裹态结果：</p>
     * <p>5 - 本对象的high大于o的high，本对象的low小于o的low</p>
     * <p>-5 - 本对象的high小于o的high，本对象的low大于o的low</p>
     */
    public int compareTo(Interval<N> o) {
        // 同边态 或 0
        if (this.getLow().compareTo(o.getLow()) == 0) {
            return this.getHigh().compareTo(o.getHigh()) * 3;
        }
        // 同边态 或 0
        if (this.getHigh().compareTo(o.getHigh()) == 0) {
            return this.getLow().compareTo(o.getLow()) * -4;
        }

        // 分离态
        if (this.getLow().compareTo(o.getHigh()) > 0) {
            return 1;
        }
        // 分离态
        if (this.getHigh().compareTo(o.getLow()) < 0) {
            return -1;
        }

        // 相交态 或 包裹态
        if (this.getHigh().compareTo(o.getHigh()) > 0) {
            if (this.getLow().compareTo(o.getLow()) > 0) {
                return 2;
            }
            if (this.getLow().compareTo(o.getLow()) < 0) {
                return 5;
            }
        }
        // 相交态 或 包裹态
        if (this.getHigh().compareTo(o.getHigh()) < 0) {
            if (this.getLow().compareTo(o.getLow()) < 0) {
                return -2;
            }
            if (this.getLow().compareTo(o.getLow()) > 0) {
                return -5;
            }
        }

        return 0;
    }

    public Node<N> getLow() {
        return low;
    }

    public void setLow(Node<N> low) {
        this.low = low;
    }

    public Node<N> getHigh() {
        return high;
    }

    public void setHigh(Node<N> high) {
        this.high = high;
    }

    public static class Node<N extends Comparable<N>> implements Comparable<Node<N>> {
        private N num;
        private Boolean eq;

        public Node(N num) {
            this.num = num;
            this.eq = true;
        }

        public Node(N num, Boolean eq) {
            this.num = num;
            this.eq = eq;
        }

        public N getNum() {
            return num;
        }

        public void setNum(N num) {
            this.num = num;
        }

        public Boolean getEq() {
            return eq;
        }

        public void setEq(Boolean eq) {
            this.eq = eq;
        }

        @Override
        public int compareTo(Node<N> o) {
            if (this.getNum().equals(o.getNum())) {
                if (this.getEq().equals(o.getEq())) {
                    return 0;
                }
                if (this.getEq()) {
                    return 1;
                } else {
                    return -1;
                }
            }
            return this.getNum().compareTo(o.getNum());
        }

        @Override
        public int hashCode() {
            return Objects.hash(getNum(), getEq());
        }

        @Override
        public boolean equals(Object obj) {
            if (obj == null) {
                return false;
            }
            if (!(obj instanceof Node)) {
                return false;
            }
            Node<N> node = (Node) obj;
            boolean result = this.getNum().equals(node.getNum());
            if (result) {
                result = this.getEq().equals(node.getEq());
            }
            return result;
        }
    }
}
