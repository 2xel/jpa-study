package org.practice;

import java.util.function.Consumer;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;

import org.practice.domain.Member;
import org.practice.domain.Team;

/**
 * Created by naver on 2018. 11. 10..
 */
public class Main {
	private static EntityManagerFactory emf;

	public static void main(String args[]) {
		emf = new AutoScanProvider().createEntityManagerFactory("jpastudy");

		run(Main::add);
//		run(Main::printUserAndTeam);
		run(Main::printUser);
		
		emf.close();

	}

	private static void add(EntityManager em) {
		Member member = new Member();
		member.setId(1);
		member.setUsername("이재민");
		
		Team team = new Team();
		team.setName("구루미");
		member.setTeam(team);

		em.persist(team);
		em.persist(member);
	}
	
	// 회원과 팀 정보를 출력하는 비즈니스 로직
	private static void printUserAndTeam(EntityManager em) {
		Member member = em.find(Member.class, 1);
		Team team = member.getTeam();
		System.out.println("회원 이름: " + member.getUsername());
	    System.out.println("소속팀: " + team.getName());
	}

	public static void printUser(EntityManager em) {
	    Member member = em.find(Member.class, 1);
	    System.out.println("회원 이름: " + member.getUsername());
	}

	private static void run(Consumer<EntityManager> runner) {
		EntityManager em = emf.createEntityManager();

		EntityTransaction tx = em.getTransaction();

		try {
			tx.begin();
			runner.accept(em);
			tx.commit();
		} catch (Exception e) {
			e.printStackTrace();
			tx.rollback();
		} finally {
			em.close();
		}
	}
}
