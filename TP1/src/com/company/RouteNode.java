package com.company;

import java.util.Objects;

//https://www.baeldung.com/java-a-star-pathfinding
public class RouteNode implements Comparable<RouteNode> {
    private Node node;
    private Node parent;
    private double routeScore;
    private double estimatedScore;

    public RouteNode (Node currentNode) {
        this(currentNode, null, Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY);
    }
    public RouteNode(Node node, Node parent, double routeScore, double estimatedScore) {
        this.node = node;
        this.parent = parent;
        this.routeScore = routeScore;
        this.estimatedScore = estimatedScore;
    }

    @Override
    public int compareTo(RouteNode otherNode) {
        return Double.compare(this.getTotalCost(), otherNode.getTotalCost());
    }

    public Node getNode() {
        return node;
    }

    public Node getParent() {
        return parent;
    }

    public double getRouteScore() {
        return routeScore;
    }

    public double getEstimatedScore() {
        return estimatedScore;
    }

    public double getTotalCost() {
        return routeScore + estimatedScore;
    }

        @Override
    public boolean equals(Object o) {
        return node.equals(o);
    }

    @Override
    public int hashCode() {
        return node.hashCode();
    }
}
