//
//  DashboardViewController.swift
//  iOS
//
//  Created by Johnny Console on 2026-01-26.
//

import UIKit

class DashboardViewController: UIViewController {
    
    private var user: User?
    
    override func viewDidLoad() {
        super.viewDidLoad()
        // Do any additional setup after loading the view.
    
    }
    
    func with(_ initUser: User) {
        user = initUser
    }
}

