package com.javacodegeeks.snippets.enterprise.dao;

import com.javacodegeeks.snippets.enterprise.Employee;

public interface EmployeeDAO {

		public void insert(Employee employee);
		public Employee findById(int id);
}
