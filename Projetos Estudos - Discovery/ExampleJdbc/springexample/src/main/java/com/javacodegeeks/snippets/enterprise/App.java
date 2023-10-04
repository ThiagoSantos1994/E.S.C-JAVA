package com.javacodegeeks.snippets.enterprise;

import java.util.ArrayList;
import java.util.List;

import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.javacodegeeks.snippets.enterprise.dao.JDBCEmployeeDAO;

public class App {

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static void main(String[] args) {
	
			ConfigurableApplicationContext context = new ClassPathXmlApplicationContext("applicationContext.xml");
//			EmployeeDAO employeeDAO = (EmployeeDAO) context.getBean("employeeDAO");
//	        Employee employee1 = new Employee(123, "javacodegeeks", 30);
//	        employeeDAO.insert(employee1);
//	        Employee employee2 = employeeDAO.findById(123);
//	        System.out.println(employee2);	
//	        
	        JDBCEmployeeDAO jdbcEmployeeDAO = (JDBCEmployeeDAO) context.getBean("jdbcEmployeeDAO");
//	        Employee employee3 = new Employee(456, "javacodegeeks", 34);
//	        jdbcEmployeeDAO.insert(employee3);
	 
//	        Employee employee4 = jdbcEmployeeDAO.findById(456);
//	        System.out.println(employee4);	
	        
//	        List<Employee> employees = jdbcEmployeeDAO.findAll();
//	        System.out.println(employees);	
	        
	        
//	        String name = jdbcEmployeeDAO.findNameById(456);
//	        System.out.println(name);	
	        
	        
	        Employee emplNew1 = new Employee(23, "John", 23);
	        Employee emplNew2 = new Employee(223, "Mark", 43);
	        List<Employee> employeesN = new ArrayList();
	        employeesN.add(emplNew1);
	        employeesN.add(emplNew2);
	        jdbcEmployeeDAO.insertBatch1(employeesN);
	        System.out.println(" inserted rows: " + employeesN);

	        System.out.println(" FindAll : " + jdbcEmployeeDAO.findAll());
	        jdbcEmployeeDAO.insertBatch2("UPDATE EMPLOYEE SET NAME ='Mary'");
	        
	        List<Employee> employees = jdbcEmployeeDAO.findAll();
	        System.out.println("Updated column name of table: " + employees);	
	        
	        System.out.println(" FindAll : " + jdbcEmployeeDAO.findAll());
			context.close();
	}
}
