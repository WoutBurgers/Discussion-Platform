# **Human-Computer Interaction**



## Introduction

In this assignment, we will be assessing the quality of our user interface using Nielsen’s usability heuristics, and making changes to improve our final product. 

Currently, our UI is getting the finishing touches, and our evaluators will be working with a finished version of the UI. The actual code behind the UI will not be fully implemented yet, meaning some aspects might not be properly evaluated. 

We will not be evaluating our own interface, as our being part of the development could lead to us being biased. Therefore, we will be asking experts to evaluate our interface, to get clear and unbiased data on usability. We will be using this data to assess what problems our interface has in terms of usability and to improve upon these to create a better end product.

## Methods

### Experts

For this evaluation, we will be recruiting 6 experts. These experts will be the members of another project group from our course. This means that they have had lectures on clear interface design and usability, and have hands-on experience designing interfaces themselves, both for this project and previous assignments. 

This means they have enough experience and knowledge to be considered experts. With this in mind, we can use the formula described by Nielsen to calculate how efficient our evaluation should be. According to Nielsen, experts will individually be able to on average identify 41% of the problems with an interface. Combining that with our total of 6 experts, we get f(6) = (1-(1-41%)i) ≈ 0,96 or 96%. This means we should be able to find 96% of the problems with our interface, from the evaluation by these 6 experts.

### Procedure

The experts are instructed to describe the usability issues found in our application by the means of a created upfront form. Such an approach provides the experts with a straightforward way of communicating to us the problems, while also keeping the procedure itself simple and easy to follow. The form consists of closed, mostly based on a 1 to 5 rating scale questions, as well as open questions allowing the evaluators to point out any issues that they can find.

The form is split into sections, where each section begins with an image of the working application, followed by the list of questions prepared for the experts. The following is an example of the questions included for the login screen of the application:

![img](https://lh6.googleusercontent.com/D-t54kpvUqBKo7IjOXCWtgkwy-WA9hoIGVxx8FKxFqCT8_inKI16818qysKago5OsMxlGD6hVI4KpIlFnn7gsfJx5OyzOB-txV4eqcW4ZrBHQnvZaEUfaz9yli6vUhTtCAlxDvIb)

As we can see, first the experts are asked to answer closed questions, then they can give some more specific feedback on the issues that they have managed to find by answering the open question.

It is important to note that the images used in the form have been taken from an already working application, hence the feedback received will be representative of that of the final product. We have, however, made further progress on the application after the Heuristic Evaluation took place, hence not all features will be properly evaluated.



The following is the full list of features that have been asked about in the form:

- Join a room

- Create a room

- Attempt to join a room with an empty username or create a room with no name

- Actions wrt. the student view:

  - Ask a question

  - Upvote a question
  - Delete a question
  - Attempt to delete someone else’s question
  - Give feedback to the lecturer

- Actions wrt. the moderator view:

  - Ban a user

  - Delete a question
  - Close the room
  - Enter the focus mode




 

### Measures

As stated above, our data collection will be in the form of the answers to the Google form. This will give us a 1-to-5 scale for different parts of our application, which allows us to quickly reflect on which parts need more work and which are good as is. Aside from the scale, we will be collecting feedback from the open questions on the form. These open questions will give us concise points of improvement

## Results

In the end, 10 experts participated in our evaluation instead of the expected 6, as we found more people willing to participate in our evaluation. A full overview of our results can be found at the bottom of this report, under the “Data collected” header. 

First, we’ll look at the 1-5 ratings, to get a general view of the quality of our project. On every question, the ratings never went lower than a three out of five, and the amount of threes never exceeds 30%. Along with this, the overall impression is 40% good, and 60% really good. This of course doesn’t mean there’s nothing for us to improve upon, but it does show we are starting off really well. 

Now for the open feedback, we can disregard a good amount of it. A lot of the replies simply state that there is nothing to improve upon. There are also some that are simply too vague like “Design of right side of the room”. The right side of the room contains quite a lot of UI elements, and no suggestion as to how to improve is given, so we choose to disregard this comment and similar ones because they don’t tell us enough. Others that we can disregard are ones that go directly against what has been asked of us, such as “Make it able to see more questions, not just top 3” in regard to the focus mode. We were tasked to implement a focus or zen mode for the lecturer where they can only see the top few questions, so that’s what we did. There is also some feedback that might be right, or might not, such as “I think the user doesn’t really need to know the feedback of the other students…”. While it isn’t exactly necessary, one could argue it’s nice to see how your peers experience a lecture, and since we already implemented it, we see no need to remove it. 

Having sifted through all our data, we are left with some very useful feedback that gives us an opportunity to improve our application. 

## Conclusion & improvements

### Conclusion

We can take away a few things from this evaluation. To begin with, we should have instructed our experts better on how to handle the open feedback questions, because a decent amount of feedback is vague, doesn’t describe a problem specifically but an area, or doesn’t suggest how to solve it. 

Secondly, there is very little overlap in problems reported by our experts, which could suggest that we haven’t found all problems yet and might not even be close to the calculated 96%. Further evaluation could reveal more problems or possible improvements to our project. 

Finally, there are definitely some improvements to be made, and this evaluation has revealed problems we would have missed otherwise. A heuristic evaluation is a great tool for improving UI design and functionality.

### Improvements

Now for the problems we gathered from our data, and how we improved them. 

The first thing we addressed as mentioned by 2 experts is the requirement to schedule a room. Previously, if the user did not set a date and/or time when creating a room, the application would throw an error informing the user that these are required fields. We fixed this, by having the application auto-fill in the current date and/or time when either or both fields are left blank. This means when left blank, the room will start at the moment of creation and is immediately usable.

The second thing we addressed was also mentioned by 2 experts and is the colour of the feedback buttons when clicked. We changed this to match our current colour scheme which matches the official TU Delft colour scheme. 

The third thing we addressed is some minor adjustments to the general UI design. In the overall impression feedback, an expert commented that some text should be bigger. While a little vague, we went over our entire UI design and adjusted some font sizes, positions of buttons and such to make it just a little nicer.

There are also some issues reported by the experts that we would’ve liked to handle, but didn’t have the time to. Having more variety in our alerts for instance when not able to join a room, the alert says the room doesn’t exist or isn’t open yet. Having two different alerts for these two different cases will make things clearer to the user. 

We would’ve also liked to do a more detailed heuristic evaluation to get more descriptive issue-reporting and with that a better idea of what we can improve. 

## Final design![img](https://lh4.googleusercontent.com/SkMhAI4QS8KwiWSZM46RMnszjL-tKUSGfUdUZZ1LLADkXbZuoSNNZmdOtrUY7g0IcFE8YlfMmyxm_zVvcuzi5Y19e-YuD3gsMYLyCXl7il1bCIITFb-bADjpfZ_aH1Ez0Bkpug3u)

After these improvements, this is our final design. 



Here you can see our login and room creation view. 



The student view![img](https://lh4.googleusercontent.com/A31pemTO4k9q2tdbC0gV0inA7dtXn1xs7B85ra3TYsHG5VyMsMh1xmd2kPUZwBgmbHoNyb6cebTO6cVBjLKcl36D8O4SKf5o1LYvSq7V2IHF2dW-t2wEy-F34s9NIUXl-Vl7UdUH)

![img](https://lh3.googleusercontent.com/vnWr-93zDAuBwuwBuoK7S5qUxbqaAhq9Rz6LaJjzl9k4agfTxCkvOC8nqF-Fhl4rQUyEx4ckhewNhME5VNHSdF_nuCUT3737Z9pdhQMLNBCn82k4gYFmsqENk9rQupmE0qJEdbu5)

The moderator view

## 

### Data collected

#### Login screen:

|                                                          | **1** | **2** | **3** | **4** | **5** |
| -------------------------------------------------------- | ----- | ----- | ----- | ----- | ----- |
| Is it clear how to join a room?                          | 0     | 0     | 0     | 3     | 7     |
| Is the layout visually pleasing?                         | 0     | 0     | 2     | 6     | 2     |
|                                                          | No    |       | Maybe |       | Yes   |
| Do you find the room creation procedure straightforward? | 0     |       | 2     |       | 8     |



Are there any other changes you would make to this scene?

- If I were to change anything; I would change the way the codes are displayed (because the part "Student code:" f.e. is showing as copy-able text, but those are minor details.

- No
- \-
- Don't oblige the user to schedule the room.
- I don't really understand the date text field and calendar dropdown (they seem duplicate)
- No, looks great
- Not putting the logo vertically
- Yes, I would allow the creation of a room without scheduling.
- Maybe place the TU logo horizontally instead of vertically
- Change pop up design.

#### Alerts in login view:

| **Ratings for multiple alerts in the login view:** | **1** | **2** | **3** | **4** | **5** |
| -------------------------------------------------- | ----- | ----- | ----- | ----- | ----- |
| No room name specified                             | 0     | 0     | 0     | 2     | 8     |
| No username filled in                              | 0     | 0     | 0     | 2     | 8     |
| Wrong room code                                    | 0     | 0     | 1     | 1     | 8     |



Are there any changes you would make to the alerts?

- No
- \-
- Don’t oblige the user to choose a room name
- Maybe have different alerts for different reasons for not being able to join a room
- No, they are very descriptive
- Maybe change the layout
- no
- Not really
- Change the design



#### Student view:

|                                            | **1** | **2** | **3** | **4** | **5** |
| ------------------------------------------ | ----- | ----- | ----- | ----- | ----- |
| How easy would it be to ask a question?    | 0     | 0     | 1     | 4     | 5     |
| How easy would it be to upvote a question? | 0     | 0     | 1     | 1     | 8     |
| How easy would it be to delete a question  | 0     | 0     | 2     | 5     | 3     |
| Is it clear how giving feedback works?     | 0     | 0     | 3     | 1     | 6     |
| Is it clear how to read the feedback?      | 0     | 0     | 3     | 2     | 5     |

Is there anything you would change?

- No
- I think the user doesn’t really need to know the feedback of the other students, it is I think only important for the lecturer
- The feedback is not clearly visible
- Colour scheme for too fast/too slow on press.
- Nothing I would change (I like your list of questions)
- No, nice design with the little pictures by feedback
- more consistent icons
- Maybe change the dark blue background when the feedback button is clicked to something else (orange or something)
- Change the design of the right side of the room



### 

#### Moderator view:

|                                                              | **1** | **2** | **3** | **4** | **5** |
| ------------------------------------------------------------ | ----- | ----- | ----- | ----- | ----- |
| Is it clear how a moderator can ban a user?                  | 0     | 0     | 1     | 4     | 5     |
| Is it clear how a moderator can delete a question?           | 0     | 0     | 0     | 4     | 6     |
| Is it clear how a moderator can close a room?                | 0     | 0     | 0     | 3     | 7     |
| Is the functionality of the moderator view clear from the screenshot? | 0     | 0     | 1     | 5     | 4     |

Is there anything you would change on the moderator view?

- No
- The view’s a bit "complicated" for the moderator, contains too many buttons and information could have been more simplistic
- Nothing really.
- nothing
- It was for me not directly clear what "show codes" and "enter focus mode" do.
- no
- Not really
- Design of right side of the room



#### Focus mode:

|                                                              | **More distracting** | **No change** | **Less distracting** |
| ------------------------------------------------------------ | -------------------- | ------------- | -------------------- |
| Does the focus mode feel less distracting than the default moderator view? | 0                    | 2             | 8                    |

Is there anything you would change on the focus mode?

- No
- There is not much distraction going on anyways (not in the default nor the focus mode), so I would not change anything.
- \-
- Nothing.
- nothing
- No, it is now clear what it does
- no
- Maybe make the screen the same size as the normal view, and just remove all the buttons that are in the normal view.
- Make it able to see more questions, not just top 3





#### Overall opinion:

What is your overall impression?

|                                  | **Very bad** | **Bad** | **Neutral** | **Good** | **Really good** |
| -------------------------------- | ------------ | ------- | ----------- | -------- | --------------- |
| What is your overall impression? | 0            | 0       | 0           | 4        | 6               |

Is there anything else you would change?

- Maybe make some text bigger, so that it is more clear to read for some users, but other than that it looks really good!
- No
- \-
- No.
- Neat looking program
- Our own design maybe, but your design looks great
- No
- NO
- Nothing to add to the things I mentioned before
- I would work more on the UI, make it more appealing. Besides this, I like your app!



------



# **Responsible Computer Science**



## Stakeholders



| **Direct stakeholders** | **Interests**                                                |
| ----------------------- | ------------------------------------------------------------ |
| Programmer              | Delivery of a working application that fulfils the clients’ requirements |
| Client                  | Commercial interest, distributing the application            |
| Designer                | A functional and aesthetically pleasing user interface       |
| Student                 | Ability to ask questions during lectures without disturbing the flow of the lecture |
| Professor               | Ability to receive questions from students and indication on what needs more explanation |

| **Indirect stakeholders** | **Interests**                                                |
| ------------------------- | ------------------------------------------------------------ |
| Governments               | Licensing, legislation                                       |
| Media                     | Possibility for education, an adaptation of modern education methods |
| Educational institutions  | More efficient communication during lectures                 |
| Parents                   | Accessibility of communication between students and lecturers |

Our main goal is of course to satisfy the client. However we are not forgetting other stakeholders, It is important to consider the view of other stakeholders and to look at things from other perspectives.

In our document, we decided to focus mainly on one indirect stakeholder - parents. 

Even though parents of students are from various backgrounds, they mostly share similar views regarding the education of their children. It’s important to them that they get good quality education in all institutes, but mainly in universities as these are specialized and oftentimes paid for. 



## Values

First of all, an important ethical value for the parents would be to have let their children have the freedom of expression. Students should be able to give their honest opinions concerning topics. According to Unesco, it’s important to have the freedom of expression to be able to share knowledge between people as they call it the “free flow of ideas and image” (Freedom of Expression: A Fundamental Human Right Underpinning All Civil Liberties, 2015). They also state that freedom of expression should be both in online and offline situations as it is a human right. Freedom of expressions makes it able for students to actively participate and stimulate their learning progress. 

Secondly, another important ethical value would be efficiency. The application that students use should be efficient for learning purposes. Parents would like to have their children study in a comfortable and encouraging atmosphere. Applications used during studying should contribute to this atmosphere. In order to achieve this goal, the applications must be efficient. 

Lastly, probably the most important ethical value for the parents is the welfare of their children. Parents want their children to have a dedicated education and want their children to feel good and study in a safe environment. It’s important to make sure the study atmosphere feels comfortable and that the application we are creating contributes to achieving this goal. 

The value we want to dig deeper into is therefore the welfare of the students or in this case the welfare of the children. We think that it is most important to make sure all students study under fine circumstances. Mental health should be taken into account but also a professional approach to studying. Achieving good welfare for students is dependent on a lot of factors; a safe studying environment, opportunities for success, enjoyment of learning and that the educational institution does everything to satisfy the personal and social necessities. (NSW Department of Education, 2020)



### Gathering of essential information

In order to be successful in providing a good environment to the students and therefore ensuring the circumstances they study in are optimal, it is essential to gather information about the needs of students from various sources.

First and foremost, asking current and former students is definitely a good starting point. Students themselves know in which situations and environments they feel best and what conditions allow them to prosper and concentrate on the studying process. 

The next step would be looking for studies on what is the ideal environment to study in and how the design of the user interface of an application influences students’ ability to learn and how it affects their concentration. 

Consulting a professional psychologist specialized in studying processes could also help point out important aspects of the study environment that affect the students’ abilities and their overall wellbeing.

## Value Hierarchy

See table below. Green is the value, blue are the norms and orange are the design requirements. 

![img](https://docs.google.com/drawings/u/0/d/sxGrcofegYXFf-H5qsIg3dg/image?w=588&h=410&rev=1&ac=1&parent=1L-I-PPpqSzoDxL2gPxu4WBAdGpao_U-cSd0nvtqxETI)







## Value tension

### Conflicts

We have now considered what is important to the parents of university students. A deep dive into the values of one stakeholder really uncovered how many things must be taken into account.

Therefore taking into consideration every possible point of view of every possible stakeholder is very difficult, probably even impossible. Every stakeholder has different values and as we start to consider new stakeholders, then their values tend to collide with values from other stakeholders.

Some of the values of parents directly go against the values of the client. The parents might want the application to have extra functionality, e.g. feedback forms to improve communication between the students and the lecturers. However, the client might want simplicity, an easy to understand user interface. Adding an extra menu option or button would make the interface more cluttered.

### Solutions

There are many solutions to this type of problem. We could ask the client to consider the point of view of a conflicting stakeholder and adjust their design requirements. Another option would be to construct the user interface in such a way that would make giving feedback unobtrusive and in line with the design requirements from the client. A more radical solution would be to weigh the severity of the conflict and if the problematic stakeholder is essential to the project. If not, the conflict could be resolved simply by omitting the conflicting values of this stakeholder – in this case not implementing an option for giving feedback.

## Conclusion

In this part of the document, we looked at definitive and possible stakeholders related to our project. Then we shifted our focus to one indirect stakeholder, namely parents of university students, and analyzed what values may be important to them in the context of our project. We also explored multiple ways of how to obtain such information, eg. interviews with students. Going even more into the depth of the value system of the parents, we focused on one value, its norms and design requirements, creating a value hierarchy. However, we can’t target only one of the stakeholders’ values, so we had to consider possible resolutions of conflicting values of different stakeholders.

In conclusion, it is good practice to consider the points of view of as many stakeholders as possible. However, it is important to structure a realistic plan, as it is not possible to satisfy the needs and values of every party that might be connected to our project.





### References

- Freedom of expression: A fundamental human right underpinning all civil liberties. (2015, April 17). UNESCO. Retrieved from: https://en.unesco.org/70years/freedom_of_expression 

- NSW Department of Education (2020), Student Welfare (PD-2002-0052-V01) Retrieved from: https://policies.education.nsw.gov.au/policy-library/policies/student-welfare-policy?refid=285835 