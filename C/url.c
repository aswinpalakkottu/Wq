#include <stdio.h>
#include <stdlib.h>
#include <string.h>

// Node structure for doubly linked list
struct Node {
    char url[100];
    struct Node *prev;
    struct Node *next;
};

// Current page pointer
struct Node *current = NULL;

// Function to create a new node
struct Node* createNode(char *url) {
    struct Node *newNode = (struct Node*)malloc(sizeof(struct Node));
    strcpy(newNode->url, url);
    newNode->prev = newNode->next = NULL;
    return newNode;
}

// Visit a new page
void visitPage(char *url) {
    struct Node *newNode = createNode(url);

    if (current != NULL) {
        // Remove forward history
        struct Node *temp = current->next;
        while (temp != NULL) {
            struct Node *del = temp;
            temp = temp->next;
            free(del);
        }
        current->next = newNode;
        newNode->prev = current;
    }
    current = newNode;

    printf("Visited: %s\n", current->url);
}

// Go back to previous page
void goBack() {
    if (current == NULL || current->prev == NULL) {
        printf("No previous page!\n");
        return;
    }
    current = current->prev;
    printf("Back to: %s\n", current->url);
}

// Go forward to next page
void goForward() {
    if (current == NULL || current->next == NULL) {
        printf("No forward page!\n");
        return;
    }
    current = current->next;
    printf("Forward to: %s\n", current->url);
}

// Show current page
void showCurrentPage() {
    if (current == NULL) {
        printf("No page visited yet!\n");
    } else {
        printf("Current page: %s\n", current->url);
    }
}

// Main function
int main() {
    int choice;
    char url[100];

    while (1) {
        printf("\n--- Web Browser Navigation ---\n");
        printf("1. Visit new page\n");
        printf("2. Go back\n");
        printf("3. Go forward\n");
        printf("4. Show current page\n");
        printf("5. Exit\n");
        printf("Enter your choice: ");
        scanf("%d", &choice);

        switch (choice) {
            case 1:
                printf("Enter URL: ");
                scanf("%s", url);
                visitPage(url);
                break;
            case 2:
                goBack();
                break;
            case 3:
                goForward();
                break;
            case 4:
                showCurrentPage();
                break;
            case 5:
                exit(0);
            default:
                printf("Invalid choice!\n");
        }
    }
    return 0;
} 