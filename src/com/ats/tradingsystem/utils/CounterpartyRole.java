package com.ats.tradingsystem.utils;

import java.util.Objects;

public class CounterpartyRole implements Comparable<CounterpartyRole> {
    private String roleCode;
    private String description;

    public CounterpartyRole(String roleCode, String description) {
        this.roleCode = roleCode;
        this.description = description;
    }

    public String getRoleCode() {
        return roleCode;
    }

    public void setRoleCode(String roleCode) {
        this.roleCode = roleCode;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public int compareTo(CounterpartyRole other) {
        return this.roleCode.compareTo(other.roleCode);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CounterpartyRole)) return false;
        CounterpartyRole that = (CounterpartyRole) o;
        return Objects.equals(roleCode, that.roleCode);
    }

    @Override
    public int hashCode() {
        return Objects.hash(roleCode);
    }

    @Override
    public String toString() {
        return roleCode;
    }
}
