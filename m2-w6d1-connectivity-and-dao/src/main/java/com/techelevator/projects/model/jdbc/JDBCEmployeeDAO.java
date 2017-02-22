package com.techelevator.projects.model.jdbc;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;

import com.techelevator.projects.model.Employee;
import com.techelevator.projects.model.EmployeeDAO;
import com.techelevator.projects.model.Project;

public class JDBCEmployeeDAO implements EmployeeDAO {

	private JdbcTemplate jdbcTemplate;

	public JDBCEmployeeDAO(DataSource dataSource) {
		this.jdbcTemplate = new JdbcTemplate(dataSource);
	}
	
	@Override
	public List<Employee> getAllEmployees() {
		ArrayList<Employee> employee = new ArrayList<>();
		String sqlGetAllEmployees = "SELECT employee_id, department_id, first_name, last_name, birth_date, gender, hire_date " +
										"FROM employee";
		
		SqlRowSet results = jdbcTemplate.queryForRowSet(sqlGetAllEmployees);
		while(results.next()) {
			Employee theEmployee = mapRowToEmployee(results);
			employee.add(theEmployee);
		}
		return employee;
	}

	@Override
	public List<Employee> searchEmployeesByName(String firstNameSearch, String lastNameSearch) {
		ArrayList<Employee> employee = new ArrayList<>();
		String sqlSearchEmployeesByName = "SELECT employee_id, department_id, first_name, last_name, birth_date, gender, hire_date " +
										"FROM employee " +
										"WHERE first_name = ? AND last_name = ?";
		SqlRowSet results = jdbcTemplate.queryForRowSet(sqlSearchEmployeesByName, firstNameSearch, lastNameSearch);
		while(results.next()) {
			Employee theEmployee = mapRowToEmployee(results);
			employee.add(theEmployee);

		}
		return employee;
	}

	@Override
	public List<Employee> getEmployeesByDepartmentId(long id) {
		ArrayList<Employee> employee = new ArrayList<>();
		String sqlGetEmployeesByDepartment = "SELECT employee_id, department_id, first_name, last_name, birth_date, gender, hire_date " +
										"FROM employee " +
										"WHERE department_id = ?";
		SqlRowSet results = jdbcTemplate.queryForRowSet(sqlGetEmployeesByDepartment, id);
		while(results.next()) {
			Employee theEmployee = mapRowToEmployee(results);
			employee.add(theEmployee);

		}
		return employee;
	}

	@Override
	public List<Employee> getEmployeesWithoutProjects() {
		ArrayList<Employee> employee = new ArrayList<>();
		String sqlGetEmployeesWithoutProjects = "SELECT  * FROM employee " +
										"WHERE employee_id NOT IN (SELECT employee_id FROM project_employee)";
										
		SqlRowSet results = jdbcTemplate.queryForRowSet(sqlGetEmployeesWithoutProjects);
		while(results.next()) {
			Employee theEmployee = mapRowToEmployee(results);
			employee.add(theEmployee);
		}
		return employee;	
	}

	@Override
	public List<Employee> getEmployeesByProjectId(Long projectId) {
		ArrayList<Employee> employee = new ArrayList<>();


		String sqlGetEmployeesByProjectId = "SELECT * FROM project_employee " + "JOIN employee ON employee.employee_id = project_employee.employee_id " +
										"WHERE project_id = ?";   
		SqlRowSet results = jdbcTemplate.queryForRowSet(sqlGetEmployeesByProjectId, projectId);
		while(results.next()) {
			Employee theEmployee = mapRowToEmployee(results);
			employee.add(theEmployee);

		}
		return employee;	
	}
	


	@Override
	public void changeEmployeeDepartment(Long employeeId, Long departmentId) {
		String sqlChangeEmployeeDepartment = "UPDATE employee " +
										"SET department_id = ? " +
										"WHERE employee_id = ?";
		jdbcTemplate.update(sqlChangeEmployeeDepartment, departmentId, employeeId);
	}
	

	@Override
	public Employee createEmployee(String firstName, String lastName, LocalDate birthDay, char gender,
			LocalDate hireDate) {
		Long id = getNextEmployeeId();
		String sqlAddEmployee = "Insert into employee  (employee_id, first_name, last_name,  birth_date, gender, " +
								"hire_date) values (?,?,?,?,?,?)";
		jdbcTemplate.update(sqlAddEmployee, id, firstName, lastName, birthDay, gender, hireDate);
		Employee theEmployee = new Employee();
		theEmployee.setId(id);
	//	theEmployee.setDepartmentId(departmentId);
		theEmployee.setFirstName(firstName);
		theEmployee.setLastName(lastName);
		theEmployee.setBirthDay(birthDay);
		theEmployee.setGender(gender);
		theEmployee.setHireDate(hireDate);
		return theEmployee;
	}

	@Override
	public void removeEmployee(Long employeeId) {
		String sqlRemoveEmployee = "DELETE FROM employee WHERE employee_id = ?";
		jdbcTemplate.update(sqlRemoveEmployee, employeeId);
		
	}
	
	private long getNextEmployeeId() {
		SqlRowSet nextIdResult = jdbcTemplate.queryForRowSet("SELECT nextval('seq_employee_id')");
		if(nextIdResult.next()) {
			return nextIdResult.getLong(1);
		} else {
			throw new RuntimeException("Something went wrong while getting an id for the new project");
		}
	}
	
	@Override
	public Employee getEmployeeById(Long employeeId){
		String sqlGetEmployeeById = "SELECT * FROM employee WHERE employee_id = ?";
		SqlRowSet results = jdbcTemplate.queryForRowSet(sqlGetEmployeeById, employeeId);
		if(results.next()){
			Employee theEmployee = mapRowToEmployee(results);
			return theEmployee;
		} else {
		return null;
		}
	}
	
	private Employee mapRowToEmployee(SqlRowSet results) {
	Employee theEmployee;
	theEmployee = new Employee();
	theEmployee.setId(results.getLong("employee_id"));
	theEmployee.setDepartmentId(results.getLong("department_id"));
	theEmployee.setFirstName(results.getString("first_name"));
	theEmployee.setLastName(results.getString("last_name"));
	theEmployee.setBirthDay(results.getDate("birth_date").toLocalDate());
	theEmployee.setGender(results.getString("gender").charAt(0));
	theEmployee.setHireDate(results.getDate("hire_date").toLocalDate());

	return theEmployee;
	}
}
