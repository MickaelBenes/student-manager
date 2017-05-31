package com.github.mickaelbenes.studentmanager.restapi.controller;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.mock.http.MockHttpOutputMessage;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;

import com.github.mickaelbenes.studentmanager.restapi.App;
import com.github.mickaelbenes.studentmanager.restapi.data.entity.Professor;
import com.github.mickaelbenes.studentmanager.restapi.data.entity.Skill;
import com.github.mickaelbenes.studentmanager.restapi.data.entity.Student;
import com.github.mickaelbenes.studentmanager.restapi.data.repository.ProfessorRepository;
import com.github.mickaelbenes.studentmanager.restapi.data.repository.SkillRepository;
import com.github.mickaelbenes.studentmanager.restapi.data.repository.StudentRepository;

@RunWith( SpringRunner.class )
@SpringBootTest( classes = App.class )
@WebAppConfiguration
public class StudentRestControllerTest {

	private MediaType contentType = new MediaType(MediaType.APPLICATION_JSON.getType(),
			MediaType.APPLICATION_JSON.getSubtype(), Charset.forName("utf8"));

	private MockMvc mockMvc;

	private HttpMessageConverter mappingJackson2HttpMessageConverter;

	private Professor professor;
	private List<Student> studentList	= new ArrayList<>();
	
	@Autowired
	private ProfessorRepository profRepo;

	@Autowired
	private StudentRepository studentRepo;
	
	@Autowired
	private SkillRepository skillRepo;

	@Autowired
	private WebApplicationContext webApplicationContext;

	@Autowired
	void setConverters( HttpMessageConverter<?>[] converters ) {
		this.mappingJackson2HttpMessageConverter = Arrays.asList( converters )
				.stream()
				.filter( hmc -> hmc instanceof MappingJackson2HttpMessageConverter )
				.findAny()
				.orElse( null );

		assertNotNull( "the JSON message converter must not be null", this.mappingJackson2HttpMessageConverter );
	}

	private static final String	USERNAME				= "mbenes";
	
	private static final String	STUDENT1_FIRSTNAME		= "First1";
	private static final String	STUDENT1_LASTNAME		= "Last1";
	private static final String	STUDENT1_SKILL1_NAME	= "Skill1";
	private static final int	STUDENT1_SKILL1_LEVEL	= 1;
	private static final String	STUDENT1_SKILL2_NAME	= "Skill2";
	private static final int	STUDENT1_SKILL2_LEVEL	= 2;
	
	private static final String	STUDENT2_FIRSTNAME		= "First2";
	private static final String	STUDENT2_LASTNAME		= "Last2";
	private static final String	STUDENT2_SKILL1_NAME	= "Skill3";
	private static final int	STUDENT2_SKILL1_LEVEL	= 3;

	@Before
	public void setup() throws Exception {
		this.mockMvc = webAppContextSetup( webApplicationContext ).build();

		this.profRepo.deleteAllInBatch();
		this.studentRepo.deleteAllInBatch();
		this.skillRepo.deleteAllInBatch();

		this.professor = profRepo.save( new Professor(USERNAME, "password", "Mickael", "Benes") );
		this.studentList.add( studentRepo.save(new Student(professor, STUDENT1_FIRSTNAME, STUDENT1_LASTNAME)) );
		this.studentList.add( studentRepo.save(new Student(professor, STUDENT2_FIRSTNAME, STUDENT2_LASTNAME)) );
		
		Skill student1Skill1	= this.skillRepo.save( new Skill(this.studentList.get(0), STUDENT1_SKILL1_NAME, STUDENT1_SKILL1_LEVEL) );
		Skill student1Skill2	= this.skillRepo.save( new Skill(this.studentList.get(0), STUDENT1_SKILL2_NAME, STUDENT1_SKILL2_LEVEL) );
		Skill student2Skill1	= this.skillRepo.save( new Skill(this.studentList.get(0), STUDENT2_SKILL1_NAME, STUDENT2_SKILL1_LEVEL) );
		this.studentList.get( 0 ).addSkill( student1Skill1 );
		this.studentList.get( 0 ).addSkill( student1Skill2 );
		this.studentList.get( 1 ).addSkill( student2Skill1 );
	}
	
	@Test
	public void userNotFound() throws Exception {
		mockMvc.perform(
					post( "/blabla/students" ).content( this.json(new Student()) ).contentType( this.contentType )
				)
				.andExpect( status().isNotFound() );
	}

	@Test
	public void readSingleStudent() throws Exception {
		mockMvc.perform( get("/" + USERNAME + "/students/" + this.studentList.get(0).getId()) )
				.andExpect( status().isOk() )
				.andExpect( content().contentType( contentType) )
				.andExpect( jsonPath("$.id", is(this.studentList.get(0).getId().intValue())) )
				.andExpect( jsonPath("$.firstName", is(STUDENT1_FIRSTNAME)) )
				.andExpect( jsonPath("$.lastName", is(STUDENT1_FIRSTNAME)))
				.andExpect( jsonPath("$.skills[0].name", is(STUDENT1_SKILL1_NAME)) )
				.andExpect( jsonPath("$.skills[0].level", is(STUDENT1_SKILL1_LEVEL)) )
				.andExpect( jsonPath("$.skills[1].name", is(STUDENT1_SKILL2_NAME)) )
				.andExpect( jsonPath("$.skills[1].level", is(STUDENT1_SKILL2_LEVEL)) );
	}

	@Test
	public void readStudents() throws Exception {
		mockMvc.perform( get("/" + USERNAME + "/students") )
				.andExpect( status().isOk() )
				.andExpect( content().contentType(contentType) )
				.andExpect( jsonPath("$", hasSize(2)) )
				.andExpect( jsonPath("$[0].id", is(this.studentList.get(0).getId().intValue())) )
				.andExpect( jsonPath("$[0].firstName", is(STUDENT1_FIRSTNAME)) )
				.andExpect( jsonPath("$[0].lastName", is(STUDENT1_LASTNAME)) )
				.andExpect( jsonPath("$[0].skills[0].id", is(this.studentList.get(0).getSkills().get(0).getId().intValue())) )
				.andExpect( jsonPath("$[0].skills[0].name", is(STUDENT1_SKILL1_NAME)) )
				.andExpect( jsonPath("$[0].skills[0].level", is(STUDENT1_SKILL1_LEVEL)) )
				.andExpect( jsonPath("$[0].skills[1].id", is(this.studentList.get(0).getSkills().get(1).getId().intValue())) )
				.andExpect( jsonPath("$[0].skills[1].name", is(STUDENT1_SKILL2_NAME)) )
				.andExpect( jsonPath("$[0].skills[1].level", is(STUDENT1_SKILL2_LEVEL)) )
				.andExpect( jsonPath("$[1].id", is(this.studentList.get(1).getId().intValue())) )
				.andExpect( jsonPath("$[1].firstName", is(STUDENT2_FIRSTNAME)) )
				.andExpect( jsonPath("$[1].lastName", is(STUDENT2_LASTNAME)) )
				.andExpect( jsonPath("$[1].skills[0].id", is(this.studentList.get(1).getSkills().get(0).getId().intValue())) )
				.andExpect( jsonPath("$[1].skills[0].name", is(STUDENT2_SKILL1_NAME)) )
				.andExpect( jsonPath("$[1].skills[0].level", is(STUDENT2_SKILL1_LEVEL)) );
	}

	@Test
	public void createStudent() throws Exception {
		String studentJson = json( new Student(this.professor, "Jean", "Lain") );

		this.mockMvc.perform(
					post( "/" + USERNAME + "/students" ).contentType( contentType ).content( studentJson )
				)
				.andExpect( status().isCreated() );
	}

	protected String json( Object o ) throws IOException {
		MockHttpOutputMessage mockHttpOutputMessage = new MockHttpOutputMessage();
		this.mappingJackson2HttpMessageConverter.write( o, MediaType.APPLICATION_JSON, mockHttpOutputMessage );
		
		return mockHttpOutputMessage.getBodyAsString();
	}

}
